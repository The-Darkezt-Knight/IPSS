document.addEventListener('DOMContentLoaded', () => {
    const syncHistoryBtn = document.getElementById('sync-history-button');
    if (syncHistoryBtn) {
        syncHistoryBtn.addEventListener('click', () => {
            window.location.href = 'surveyor.html';
        });
    }

    const form = document.getElementById('survey-form');
    if (!form) {
        return;
    }

    const regionSelect = document.getElementById('regionCode');
    const provinceSelect = document.getElementById('provinceCode');
    const citySelect = document.getElementById('cityMunicipalityCode');
    const baranggaySelect = document.getElementById('baranggayCode');
    const submitBtn = form.querySelector('button[type="submit"]');

    const setOptions = (select, options, placeholder) => {
        if (!select) {
            return;
        }

        select.innerHTML = '';
        const placeholderOption = document.createElement('option');
        placeholderOption.value = '';
        placeholderOption.textContent = placeholder;
        select.appendChild(placeholderOption);

        options.forEach((option) => {
            const el = document.createElement('option');
            el.value = option.value;
            el.textContent = option.label;
            select.appendChild(el);
        });
    };

    const fetchJson = async (url, options = {}) => {
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };
        const response = await fetch(url, { ...options, headers });
        if (!response.ok) {
            const message = await response.text();
            throw new Error(message || `Request failed (${response.status})`);
        }

        return response.json();
    };

    const loadRegions = async () => {
        try {
            const regions = await fetchJson('/api/location/region/all', { method: 'GET' });
            const options = regions.map((region) => ({
                value: region.id,
                label: region.name
            }));
            setOptions(regionSelect, options, 'Select region');
        } catch (error) {
            console.error(error);
            setOptions(regionSelect, [], 'Failed to load regions');
        }
    };

    const loadProvinces = async (regionName) => {
        try {
            const provinces = await fetchJson('/api/location/province/code', {
                method: 'POST',
                body: JSON.stringify({ regionName })
            });
            const options = provinces.map((province) => ({
                value: province.id,
                label: province.name
            }));
            setOptions(provinceSelect, options, 'Select province');
        } catch (error) {
            console.error(error);
            setOptions(provinceSelect, [], 'Failed to load provinces');
        }
    };

    const loadCities = async (provinceName) => {
        try {
            const cities = await fetchJson('/api/location/cityMunicipality/code', {
                method: 'POST',
                body: JSON.stringify({ provinceName })
            });
            const options = cities.map((city) => ({
                value: city.id,
                label: city.name
            }));
            setOptions(citySelect, options, 'Select city / municipality');
        } catch (error) {
            console.error(error);
            setOptions(citySelect, [], 'Failed to load cities');
        }
    };

    const loadBaranggays = async (cityMunicipalityName) => {
        try {
            const baranggays = await fetchJson('/api/location/baranggay/all', {
                method: 'POST',
                body: JSON.stringify({ cityMunicipalityName })
            });
            const options = baranggays.map((baranggay) => ({
                value: baranggay.id,
                label: baranggay.name
            }));
            setOptions(baranggaySelect, options, 'Select baranggay');
        } catch (error) {
            console.error(error);
            setOptions(baranggaySelect, [], 'Failed to load baranggays');
        }
    };

    if (regionSelect) {
        regionSelect.addEventListener('change', async () => {
            setOptions(provinceSelect, [], 'Select province');
            setOptions(citySelect, [], 'Select city / municipality');
            setOptions(baranggaySelect, [], 'Select baranggay');

            const regionName = regionSelect.selectedOptions[0]?.textContent?.trim();
            if (!regionSelect.value || !regionName) {
                return;
            }

            await loadProvinces(regionName);
        });
    }

    if (provinceSelect) {
        provinceSelect.addEventListener('change', async () => {
            setOptions(citySelect, [], 'Select city / municipality');
            setOptions(baranggaySelect, [], 'Select baranggay');

            const provinceName = provinceSelect.selectedOptions[0]?.textContent?.trim();
            if (!provinceSelect.value || !provinceName) {
                return;
            }

            await loadCities(provinceName);
        });
    }

    if (citySelect) {
        citySelect.addEventListener('change', async () => {
            setOptions(baranggaySelect, [], 'Select baranggay');

            const cityName = citySelect.selectedOptions[0]?.textContent?.trim();
            if (!citySelect.value || !cityName) {
                return;
            }

            await loadBaranggays(cityName);
        });
    }

    const textOrDefault = (value, fallback = 'N/A') => {
        const text = String(value || '').trim();
        return text.length === 0 ? fallback : text;
    };

    const numberOrDefault = (value, fallback) => {
        const parsed = parseFloat(String(value || '').trim());
        return Number.isNaN(parsed) ? fallback : parsed;
    };

    const requireValue = (value, label) => {
        if (!value) {
            alert(`${label} is required.`);
            return false;
        }
        return true;
    };

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        if (!requireValue(regionSelect?.value, 'Region')) {
            return;
        }
        if (!requireValue(provinceSelect?.value, 'Province')) {
            return;
        }
        if (!requireValue(citySelect?.value, 'City / Municipality')) {
            return;
        }
        if (!requireValue(baranggaySelect?.value, 'Baranggay')) {
            return;
        }

        const formData = new FormData(form);
        const birthdateRaw = String(formData.get('birthdate') || '').trim();
        const birthdate = birthdateRaw || new Date().toISOString().slice(0, 10);
        const birthYearRaw = parseInt(String(formData.get('birthYear') || '').trim(), 10);
        const birthYear = Number.isNaN(birthYearRaw) ? parseInt(birthdate.slice(0, 4), 10) : birthYearRaw;

        const msmeClassification = String(formData.get('msmeClassification') || '').trim();
        const civilStatus = String(formData.get('civilStatus') || '').trim();
        const sex = String(formData.get('sex') || '').trim();

        if (!requireValue(msmeClassification, 'MSME classification')) {
            return;
        }
        if (!requireValue(civilStatus, 'Civil status')) {
            return;
        }
        if (!requireValue(sex, 'Sex')) {
            return;
        }

        const payload = {
            id: textOrDefault(formData.get('id')),
            oldId: textOrDefault(formData.get('oldId')),
            statusOfClient: textOrDefault(formData.get('statusOfClient')),
            specifyLevel: textOrDefault(formData.get('specifyLevel')),
            categoryOfClient: textOrDefault(formData.get('categoryOfClient')),
            socialClassification: textOrDefault(formData.get('socialClassification')),
            diffAbledType: textOrDefault(formData.get('diffAbledType')),
            isSenior: String(formData.get('isSenior') || '').toLowerCase() === 'true',
            isIndigeneous: String(formData.get('isIndigeneous') || '').toLowerCase() === 'true',
            levelOfDigitalization: textOrDefault(formData.get('levelOfDigitalization')),
            digitalTools: textOrDefault(formData.get('digitalTools')),
            msmeClassification,
            clientDesignation: textOrDefault(formData.get('clientDesignation')),
            firstName: textOrDefault(formData.get('firstName')),
            middleName: textOrDefault(formData.get('middleName')),
            lastName: textOrDefault(formData.get('lastName')),
            suffix: textOrDefault(formData.get('suffix')),
            civilStatus,
            sex,
            birthdate,
            birthYear,
            citizenship: textOrDefault(formData.get('citizenship')),
            dtiKonekId: textOrDefault(formData.get('dtiKonekId')),
            philippineIdentificationSystem: textOrDefault(formData.get('philippineIdentificationSystem')),
            regionCode: regionSelect.value,
            provinceCode: provinceSelect.value,
            cityMunicipalityCode: citySelect.value,
            baranggayCode: baranggaySelect.value,
            district: textOrDefault(formData.get('district')),
            zipCode: textOrDefault(formData.get('zipCode')),
            address: textOrDefault(formData.get('address')),
            latitude: numberOrDefault(formData.get('latitude'), 0),
            longitude: numberOrDefault(formData.get('longitude'), 0),
            landlineNumber: textOrDefault(formData.get('landlineNumber')),
            faxNumber: textOrDefault(formData.get('faxNumber')),
            mobileNumber: textOrDefault(formData.get('mobileNumber')),
            emailAddress: textOrDefault(formData.get('emailAddress')),
            socialMedia: textOrDefault(formData.get('socialMedia')),
            website: textOrDefault(formData.get('website')),
            eCommercePlatform: textOrDefault(formData.get('eCommercePlatform'))
        };

        if (!submitBtn) {
            return;
        }

        const originalText = submitBtn.innerText;
        submitBtn.innerText = 'Submitting...';
        submitBtn.disabled = true;

        try {
            if (typeof saveClient === 'function') {
                // Use offline-first strategy
                await saveClient(payload);
                if (navigator.onLine) {
                    alert('Survey data submitted successfully!');
                } else {
                    alert('Survey data saved locally. It will be synced when you are online.');
                }
            } else {
                // Fallback to direct network request
                const response = await fetch('/api/client/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const message = await response.text();
                    throw new Error(message || `Request failed (${response.status})`);
                }
                alert('Survey data submitted successfully!');
            }

            form.reset();
            setOptions(provinceSelect, [], 'Select province');
            setOptions(citySelect, [], 'Select city / municipality');
            setOptions(baranggaySelect, [], 'Select baranggay');
            await loadRegions();
        } catch (error) {
            console.error(error);
            alert(`Submission failed: ${error.message}`);
        } finally {
            submitBtn.innerText = originalText;
            submitBtn.disabled = false;
        }
    });

    const saveForSyncBtn = document.getElementById('save-for-sync-btn');
    if (saveForSyncBtn) {
        saveForSyncBtn.addEventListener('click', () => {
            // Trigger the form submit event manually to run validations and payload generation
            form.dispatchEvent(new Event('submit', { cancelable: true, bubbles: true }));
        });
    }

    loadRegions();
});