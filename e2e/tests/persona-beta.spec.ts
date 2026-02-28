import { expect, test } from '@playwright/test';

async function submitDefaultVerdict(page: any, cityLabel = 'New York, NY', rent = '3000', cash = '15000') {
  await page.goto('/RentVerdict/');
  await expect(page.locator('h1')).toContainText('Know Your');
  await page.selectOption('#cityState', { label: cityLabel });
  await page.fill('#monthlyRent', rent);
  await page.fill('#availableCash', cash);
  await page.click('button[type="submit"]');
  await expect(page).toHaveURL(/\/RentVerdict\/verdict/);
}

test.describe('Persona Beta - First-time renter journey', () => {
  test('First-time renter can calculate and lands on noindex result', async ({ page }) => {
    await submitDefaultVerdict(page);

    await expect(page.locator('body')).toContainText(/APPROVED|BORDERLINE|DENIED/);
    await expect(page.locator('body')).toContainText('Verified Cost Breakdown');

    const robotsMeta = page.locator('meta[name="robots"]');
    await expect(robotsMeta).toHaveAttribute('content', /noindex, nofollow/);
  });

  test('Poor-credit user sees intent landing and demo provider disclosures', async ({ page }) => {
    await page.goto('/RentVerdict/verdict/credit/poor/new-york-ny');
    await expect(page.locator('h1')).toContainText('Poor Credit');
    await expect(page.locator('body')).toContainText('Demo provider links are shown for UX testing only');
    await expect(page.getByRole('link', { name: 'Open Demo Guarantor Provider' })).toBeVisible();
  });

  test('Relocation persona can trigger long-distance simulation', async ({ page }) => {
    await submitDefaultVerdict(page, 'Austin, TX', '2200', '12000');

    const longDistanceToggle = page.locator('.toggle-pill').filter({ hasText: 'Long Distance' }).first();
    await longDistanceToggle.click();

    const locationSelect = page.locator('#sim-from-location');
    await expect(locationSelect).toBeVisible();
    await locationSelect.selectOption({ index: 1 });

    await expect(page.locator('#distance-preview')).toBeVisible();
    await expect(page.locator('#distance-miles')).not.toHaveText('0');
  });

  test('Long-distance simulation warns when origin is missing', async ({ page }) => {
    await submitDefaultVerdict(page, 'Austin, TX', '2200', '12000');

    const longDistanceToggle = page.locator('.toggle-pill').filter({ hasText: 'Long Distance' }).first();
    await longDistanceToggle.click();

    const feedback = page.locator('#sim-feedback');
    await expect(feedback).toBeVisible();
    await expect(feedback).toContainText('Select an origin city for long-distance simulation.');
  });
});
