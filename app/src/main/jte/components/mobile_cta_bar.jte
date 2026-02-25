@param String baseUrl = "https://movecostinfo.com"

<!-- Floating CTA Bar (Mobile Only, hidden by default) -->
<div id="mobile-cta-bar" class="floating-cta-bar d-md-none" style="display: none;">
    <div style="color: white;">
        <div style="font-size: 0.75rem; opacity: 0.8; font-weight: 600;">APPROVED OR DENIED?</div>
        <div style="font-size: 0.9rem; font-weight: 700;">Check Your Potential</div>
    </div>
    <a href="/RentVerdict/" class="btn btn-primary btn-sm" style="padding: 0.75rem 1.5rem; font-weight: 700;">START AUDIT</a>
</div>

<!-- Mobile Floating Bar Scroll Logic -->
<script>
(function() {
    const bar = document.getElementById('mobile-cta-bar');
    if (!bar) return;
    
    window.addEventListener('scroll', function() {
        if (window.scrollY > 300) {
            bar.style.display = 'flex';
        } else {
            bar.style.display = 'none';
        }
    });
})();
</script>
