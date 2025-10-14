// === Smooth scroll for footer and navbar ===
function smoothScroll(selector) {
    const target = document.querySelector(selector);
    if (target) {
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

// Footer links
document.querySelectorAll('footer .link').forEach(link => {
    link.addEventListener('click', e => {
        e.preventDefault();
        smoothScroll(link.getAttribute('href'));
    });
});

// Navbar buttons
document.querySelectorAll('.nav-link').forEach(btn => {
    btn.addEventListener('click', () => {
        smoothScroll('#' + btn.getAttribute('onclick').split("'")[1]);
    });
});

// === Fade-in + pop animation on scroll ===
const cards = document.querySelectorAll('.card');

const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('show');
            observer.unobserve(entry.target);
        }
    });
}, { threshold: 0.2 });

cards.forEach(card => observer.observe(card));

// === Click-to-zoom effect with overlay ===
const overlay = document.createElement('div');
overlay.classList.add('overlay');
document.body.appendChild(overlay);

cards.forEach(card => {
    card.addEventListener('click', () => {
        card.classList.add('zoomed');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden'; // prevent background scroll

        // Save original position and z-index
        card.dataset.originalPosition = card.style.position || '';
        card.dataset.originalZ = card.style.zIndex || '';
    });
});

// Close zoom on overlay click or ESC
function closeZoom() {
    document.querySelectorAll('.card.zoomed').forEach(card => {
        card.classList.remove('zoomed');
        card.style.position = card.dataset.originalPosition;
        card.style.zIndex = card.dataset.originalZ;
    });
    overlay.classList.remove('active');
    document.body.style.overflow = ''; // restore scroll
}

overlay.addEventListener('click', closeZoom);
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeZoom();
});