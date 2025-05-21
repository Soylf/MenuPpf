document.addEventListener('DOMContentLoaded', function() {
  const contactBtn = document.querySelector('.btn-contact-1');
  const contactOverlay = document.getElementById('contact-overlay');
  const contactCloseBtn = document.querySelector('.contact-close-btn');

  if (contactBtn && contactOverlay) {
    contactBtn.addEventListener('click', function() {
      contactOverlay.classList.remove('hidden');
      document.body.style.overflow = 'hidden';
    });

    contactCloseBtn.addEventListener('click', function() {
      contactOverlay.classList.add('hidden');
      document.body.style.overflow = '';
    });

    contactOverlay.addEventListener('click', function(e) {
      if (e.target === contactOverlay) {
        contactOverlay.classList.add('hidden');
        document.body.style.overflow = '';
      }
    });

    document.addEventListener('keydown', function(e) {
      if (e.key === 'Escape' && !contactOverlay.classList.contains('hidden')) {
        contactOverlay.classList.add('hidden');
        document.body.style.overflow = '';
      }
    });
  }
});
