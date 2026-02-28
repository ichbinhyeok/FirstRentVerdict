import { expect, test } from '@playwright/test';

const representativePages = [
  { path: '/RentVerdict/cities', label: 'cities hub' },
  { path: '/RentVerdict/verdict/new-york-ny', label: 'city landing' },
  { path: '/RentVerdict/verdict/credit/poor/new-york-ny', label: 'credit landing' },
  { path: '/RentVerdict/verdict/moving-to/austin-tx', label: 'relocation landing' },
  { path: '/RentVerdict/verdict/moving-from/los-angeles-ca/to/austin-tx', label: 'relocation pair' },
];

test.describe('UI Regression - representative URLs', () => {
  test('representative pages render without horizontal breakage', async ({ page }) => {
    const pageErrors: string[] = [];
    page.on('pageerror', (err) => pageErrors.push(err.message));

    for (const target of representativePages) {
      await test.step(`visit ${target.label}: ${target.path}`, async () => {
        await page.goto(target.path, { waitUntil: 'domcontentloaded' });
        await expect(page.locator('h1').first()).toBeVisible();

        const hasHorizontalOverflow = await page.evaluate(() => {
          const root = document.documentElement;
          return root.scrollWidth - window.innerWidth > 1;
        });
        expect(hasHorizontalOverflow).toBeFalsy();

        const visibleBodyHeight = await page.evaluate(() => document.body.scrollHeight);
        expect(visibleBodyHeight).toBeGreaterThan(600);

        const ctaCount = await page.locator('a.btn, button.btn').count();
        expect(ctaCount).toBeGreaterThan(0);
      });
    }

    expect(pageErrors, `runtime page errors: ${pageErrors.join(' | ')}`).toEqual([]);
  });

  test('IRS migration context blocks are visible on relocation pages', async ({ page }) => {
    await page.goto('/RentVerdict/verdict/moving-to/austin-tx');
    await expect(page.locator('body')).toContainText('Top Inbound Origins to TX');
    await expect(page.locator('body')).toContainText('IRS 2021-2022');

    await page.goto('/RentVerdict/verdict/moving-from/los-angeles-ca/to/austin-tx');
    await expect(page.locator('body')).toContainText('IRS Corridor Check (CA -> TX)');
    await expect(page.locator('body')).toContainText('top inbound corridors');
  });
});
