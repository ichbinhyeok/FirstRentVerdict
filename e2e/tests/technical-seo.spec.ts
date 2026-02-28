import { expect, test } from '@playwright/test';

test.describe('Technical SEO and route hygiene', () => {
  test('canonical dotted slug returns 301 and target location', async ({ request }) => {
    const res = await request.get('/RentVerdict/verdict/st.-louis-mo', { maxRedirects: 0 });
    expect(res.status()).toBe(301);
    const location = res.headers()['location'] || '';
    expect(location).toContain('/RentVerdict/verdict/st-louis-mo');
  });

  test('compare placeholder is 410 Gone', async ({ request }) => {
    const res = await request.get('/RentVerdict/verdict/compare/austin-tx-vs-new-york-ny');
    expect(res.status()).toBe(410);
  });

  test('robots and sitemap are clean', async ({ request }) => {
    const robots = await request.get('/robots.txt');
    expect(robots.status()).toBe(200);
    const robotsText = await robots.text();
    expect(robotsText).toContain('Sitemap:');

    const sitemap = await request.get('/sitemap.xml');
    expect(sitemap.status()).toBe(200);
    const xml = await sitemap.text();
    expect(xml).toContain('/RentVerdict/verdict/st-louis-mo');
    expect(xml).not.toContain('/RentVerdict/verdict/st.-louis-mo');
    expect(xml).not.toContain('/verdict/compare/');
    expect(xml).not.toContain('/moving-from/new-york-ny/to/new-york-ny');
  });

  test('simulate API rejects invalid payloads', async ({ request }) => {
    const badCity = await request.post('/RentVerdict/api/simulate', {
      data: {
        city: 'Fake City',
        state: 'NY',
        monthlyRent: 3000,
        availableCash: 15000,
        hasPet: false,
        isLocalMove: true,
        creditTier: 'GOOD',
      },
    });
    expect(badCity.status()).toBe(400);

    const missingOrigin = await request.post('/RentVerdict/api/simulate', {
      data: {
        city: 'New York',
        state: 'NY',
        monthlyRent: 3000,
        availableCash: 15000,
        hasPet: false,
        isLocalMove: false,
        creditTier: 'GOOD',
      },
    });
    expect(missingOrigin.status()).toBe(400);
  });
});
