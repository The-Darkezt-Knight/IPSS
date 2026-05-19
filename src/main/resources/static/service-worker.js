importScripts('/js/surveyor/offlineSync.js');

const CACHE_NAME = 'surveyor-cache-v1';

self.addEventListener('install', (event) => {
    self.skipWaiting();
});

self.addEventListener('activate', (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames.map((cacheName) => {
                    if (cacheName !== CACHE_NAME) {
                        return caches.delete(cacheName);
                    }
                })
            );
        }).then(() => clients.claim())
    );
});

self.addEventListener('sync', (event) => {
    if (event.tag === 'sync-clients') {
        event.waitUntil(syncPendingRecords());
    }
});

self.addEventListener('fetch', (event) => {
    const url = new URL(event.request.url);

    if (url.pathname.startsWith('/api/location/')) {
        event.respondWith(handleLocationRequest(event.request));
    } else if (event.request.method === 'GET' && !url.pathname.startsWith('/api/')) {
        event.respondWith(handleStaticAsset(event.request));
    }
});

async function handleLocationRequest(request) {
    let cacheKey = request.url;
    let bodyText = '';

    if (request.method === 'POST') {
        bodyText = await request.clone().text();
        cacheKey += '?' + encodeURIComponent(bodyText);
    }

    const cache = await caches.open(CACHE_NAME);

    try {
        const response = await fetch(request.clone());
        if (response.ok) {
            // Store response against a GET request to allow caching
            cache.put(new Request(cacheKey, { method: 'GET' }), response.clone());
        }
        return response;
    } catch (error) {
        const cachedResponse = await cache.match(new Request(cacheKey, { method: 'GET' }));
        if (cachedResponse) {
            return cachedResponse;
        }
        throw error; // If offline and not in cache, throw
    }
}

async function handleStaticAsset(request) {
    const cache = await caches.open(CACHE_NAME);
    try {
        // Try network first to keep it up to date
        const fetchRes = await fetch(request);
        if (fetchRes.ok) {
            cache.put(request, fetchRes.clone());
        }
        return fetchRes;
    } catch (error) {
        // Offline fallback
        const cachedRes = await cache.match(request);
        if (cachedRes) {
            return cachedRes;
        }
        throw error;
    }
}
