// Theme toggle
function toggleTheme() {
  const html = document.documentElement;
  const next = html.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
  html.setAttribute('data-theme', next);
  localStorage.setItem('devlog-theme', next);
}

// Init on load (prevent FOUC — also runs inline in <head>)
(function () {
  const saved = localStorage.getItem('devlog-theme');
  if (saved) document.documentElement.setAttribute('data-theme', saved);
})();

// 댓글 인라인 수정
document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('.btn-edit-comment').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var cuid = this.dataset.cuid;
      document.getElementById('comment-body-' + cuid).style.display = 'none';
      var form = document.getElementById('edit-form-' + cuid);
      form.style.display = 'flex';
      form.querySelector('textarea').focus();
    });
  });

  document.querySelectorAll('.btn-cancel-edit').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var cuid = this.dataset.cuid;
      document.getElementById('comment-body-' + cuid).style.display = '';
      document.getElementById('edit-form-' + cuid).style.display = 'none';
    });
  });
});

// Section nav active highlight on scroll
document.addEventListener('DOMContentLoaded', function () {
  const sections = document.querySelectorAll('.main-section[id]');
  const navItems = document.querySelectorAll('.snav-item');
  if (!sections.length || !navItems.length) return;

  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        navItems.forEach(a => a.classList.remove('active'));
        const active = document.querySelector('.snav-item[href="#' + entry.target.id + '"]');
        if (active) active.classList.add('active');
      }
    });
  }, { rootMargin: '-50% 0px -45% 0px' });

  sections.forEach(s => observer.observe(s));
});
