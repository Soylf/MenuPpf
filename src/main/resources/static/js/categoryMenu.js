export async function initCategoryMenu() {
    try {
        const response = await fetch('/admin/category');
        if (!response.ok) throw new Error(`Ошибка HTTP (${response.status})`);
        const categories = await response.json();
        createCategoryMenu(categories);
    } catch (err) {
        console.error("Ошибка при получении категорий:", err.message);
    }
}

function createCategoryMenu(categories) {
    const menuSite = document.querySelector('.menu-site');
    if (!menuSite) return;

    const dropdown = document.getElementById('dropdown');
    const toggleBtn = document.getElementById('toggle-btn');
    const menuIcon = document.getElementById('menu-icon');
    if (!dropdown || !toggleBtn || !menuIcon) return;

    categories.forEach(category => {
        const btn = document.createElement('button');
        btn.textContent = category;
        btn.className = "dropdown-btn";

        btn.addEventListener('click', () => {
            const section = document.getElementById(category.toLowerCase().replace(/\s+/g, '-'));
            if (section) {
                section.scrollIntoView({ behavior: 'smooth' });
            } else {
                console.warn(`Раздел с заголовком "${category}" не найден.`);
            }
        });

        dropdown.appendChild(btn);
    });

    toggleBtn.addEventListener('click', () => {
        dropdown.classList.toggle('show');
        dropdown.classList.toggle('hidden');

        if (dropdown.classList.contains('show')) {
            menuIcon.classList.remove('fa-chevron-down');
            menuIcon.classList.add('fa-chevron-up');
        } else {
            menuIcon.classList.remove('fa-chevron-up');
            menuIcon.classList.add('fa-chevron-down');
        }
    });

    document.addEventListener('click', (e) => {
        if (!menuSite.contains(e.target)) {
            dropdown.classList.add('hidden');
            dropdown.classList.remove('show');
            menuIcon.classList.remove('fa-chevron-up');
            menuIcon.classList.add('fa-chevron-down');
        }
    });
}
