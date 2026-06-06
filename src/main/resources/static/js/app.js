// ============================================
// Sistema de Evento Esportivo - JavaScript
// ============================================

document.addEventListener('DOMContentLoaded', function() {

    // ============================================
    // Mobile Navigation Toggle
    // ============================================
    const navToggle = document.querySelector('.navbar-toggle');
    const navMenu = document.querySelector('.navbar-nav');

    if (navToggle && navMenu) {
        navToggle.addEventListener('click', function() {
            navMenu.classList.toggle('active');
        });

        // Close menu on link click
        navMenu.querySelectorAll('a').forEach(function(link) {
            link.addEventListener('click', function() {
                navMenu.classList.remove('active');
            });
        });
    }

    // ============================================
    // Fade-in Animation on Scroll
    // ============================================
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    document.querySelectorAll('.card, .match-card, .city-card, .team-card, .stat-card, .bracket-match, .admin-match-item').forEach(function(el) {
        observer.observe(el);
    });

    // ============================================
    // Filter Form Auto-submit
    // ============================================
    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        filterForm.querySelectorAll('select, input[type="date"]').forEach(function(el) {
            el.addEventListener('change', function() {
                filterForm.submit();
            });
        });
    }

    // ============================================
    // Admin - Score Input Validation
    // ============================================
    document.querySelectorAll('.admin-form input[type="number"]').forEach(function(input) {
        input.addEventListener('input', function() {
            if (this.value < 0) this.value = 0;
            if (this.value > 99) this.value = 99;
        });
    });

    // ============================================
    // Smooth counter animation for stats
    // ============================================
    function animateCounter(element, target) {
        let current = 0;
        const increment = target / 30;
        const timer = setInterval(function() {
            current += increment;
            if (current >= target) {
                current = target;
                clearInterval(timer);
            }
            element.textContent = Math.floor(current);
        }, 30);
    }

    const statObserver = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                const target = parseInt(entry.target.dataset.count);
                if (target && !entry.target.dataset.animated) {
                    entry.target.dataset.animated = 'true';
                    animateCounter(entry.target, target);
                }
                statObserver.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });

    document.querySelectorAll('.stat-number[data-count]').forEach(function(el) {
        statObserver.observe(el);
    });

    // ============================================
    // Alert auto-dismiss
    // ============================================
    document.querySelectorAll('.alert').forEach(function(alert) {
        setTimeout(function() {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(function() {
                alert.remove();
            }, 300);
        }, 5000);
    });

    // ============================================
    // Active nav link highlight
    // ============================================
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar-nav a').forEach(function(link) {
        const href = link.getAttribute('href');
        if (href === currentPath || (href !== '/' && currentPath.startsWith(href))) {
            link.classList.add('active');
        }
    });
});
