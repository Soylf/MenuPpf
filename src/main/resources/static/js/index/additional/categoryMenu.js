export async function initCategoryMenu() {
    try {
        const response = await fetch('/items/category');
        if (!response.ok) throw new Error(`Ошибка HTTP (${response.status})`);
        const categories = await response.json();
        createCategoryMenu(categories);
    } catch (err) {
        console.error("Ошибка при получении категорий:", err.message);
    }
}

function updateToggleButton() {
    const name = localStorage.getItem('widgetName') || "";
    const toggleBtn = document.getElementById('toggle-btn');
    if (toggleBtn) {
        toggleBtn.innerHTML = `
            <span>${name}</span>
            <i class="fas fa-chevron-down" id="menu-icon"></i>
        `;
    }
}

function createCategoryMenu(categories) {
    const menuUpSite = document.querySelector('.menu-toggle-wrapper');
    if (!menuUpSite) return;

    const dropdown  = document.getElementById('dropdown');
    const toggleBtn = document.getElementById('toggle-btn');
    const menuIcon  = document.getElementById('menu-icon');

    if (!dropdown || !toggleBtn || !menuIcon) return;
    updateToggleButton();

    categories.forEach(category => {
        const btn = document.createElement('button');
        btn.textContent = category;
        btn.className   = "dropdown-btn";

        btn.addEventListener('click', () => {
            const id = category.toLowerCase().replace(/\s+/g, '-');
            const section = document.getElementById(id);
            if (section) section.scrollIntoView({ behavior: 'smooth' });
            dropdown.classList.remove('show');
            toggleBtn.classList.remove('open');
            menuIcon.classList.replace('fa-chevron-up','fa-chevron-down');
        });

        dropdown.appendChild(btn);
    });

    toggleBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        dropdown.classList.toggle('show');
        toggleBtn.classList.toggle('open');
        menuIcon.classList.toggle('fa-chevron-up');
        menuIcon.classList.toggle('fa-chevron-down');
    });

    document.addEventListener('click', (e) => {
        if (!menuUpSite.contains(e.target)) {
            dropdown.classList.remove('show');
            toggleBtn.classList.remove('open');
            menuIcon.classList.replace('fa-chevron-up','fa-chevron-down');
        }
    });

    window.addEventListener('storage', (event) => {
            if (event.key === 'widgetName') {
                const newName = event.newValue || "";
                updateToggleButton(newName);
            }
    });
}