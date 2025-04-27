document.addEventListener('DOMContentLoaded', () => {
    let cart = [];

  function groupByCategory(items) {
    return items.reduce((acc, currentItem) => {
      const categoryName = currentItem.category || 'Без категории';
      acc[categoryName] = [...(acc[categoryName] || []), currentItem];
      return acc;
    }, {});
  }

  function createCategorySection(categoryName, itemsInCategory) {
    const sectionContainer = document.createElement('section');
    sectionContainer.classList.add('category-section');

    const header = document.createElement('h2');
    header.textContent = categoryName;
    sectionContainer.appendChild(header);

    const menuGrid = document.createElement('div');
    menuGrid.classList.add('menu-grid');
    sectionContainer.appendChild(menuGrid);

    itemsInCategory.forEach(item => {
      const itemCard = document.createElement('div');
      itemCard.classList.add('menu-item');

      let imageUrl = item.image ? item.image : '/path/to/default-image.jpg';

      itemCard.innerHTML = `
        <div class="image-wrapper">
          <img src="${imageUrl}" alt="${item.name}" class="main-image">
        </div>

        <h3>${item.name}</h3>
        <p>${item.description || 'Не указанно'}</p>

        <div style="display:flex; justify-content:space-between; align-items:center;">
          <p class="price">Цена: ${item.price} ₽</p>

          <button type="button" class="add-to-cart-btn" data-id="${item.id}" data-name="${item.name}" data-price="${item.price}">
            <span class="plus-sign">+</span>
          </button>

          <div style="border-left: 1px solid #ccc; padding-left: 10px; font-size: 16px; color:#343434;">
            <strong>Кол-во:</strong> ${item.pieces || 'Не указанно'}
            <br />
            <strong>Вес:</strong> ${item.heft || 'Не указанно'} ккал
          </div>
        </div>
      `;

      menuGrid.appendChild(itemCard);
    });

    return sectionContainer;
  }

  // Отображение всего каталога товаров
  function displayMenu(items) {
    const groupedItems = groupByCategory(items);
    const container = document.getElementById('menu-container');
    container.innerHTML = '';

    Object.keys(groupedItems).forEach(category => {
      const categorySection = createCategorySection(category, groupedItems[category]);
      container.appendChild(categorySection);
    });
  }

  // Получаем список всех товаров от сервера
  async function fetchItems() {
    try {
      const response = await fetch('/admin/items');
      if (!response.ok) throw new Error(`Ошибка HTTP (${response.status})`);

      const items = await response.json();
      displayMenu(items);
      document.querySelectorAll('.add-to-cart-btn').forEach(button => {
                      button.addEventListener('click', () => {
                          const itemId = button.getAttribute('data-id');
                          const itemName = button.getAttribute('data-name');
                          const itemPrice = parseFloat(button.getAttribute('data-price'));

                          // Проверяем, есть ли уже такой товар в корзине
                          const existingItem = cart.find(item => item.id === itemId);

                          if (existingItem) {
                              existingItem.quantity++;
                          } else {
                              cart.push({ id: itemId, name: itemName, price: itemPrice, quantity: 1 });
                          }

                          updateCart();
                      });
                  });
    } catch (err) {
      console.error("Ошибка:", err.message);
    }
  }

function updateCart() {
        const cartCount = document.getElementById('cart-count');
        const cartSum = document.getElementById('cart-sum');

        let totalQuantity = 0;
        let totalPrice = 0;

        cart.forEach(item => {
            totalQuantity += item.quantity;
            totalPrice += item.price * item.quantity;
        });

        cartCount.textContent = totalQuantity;
        cartSum.textContent = totalPrice + ' ₽';
    }

    fetchItems();
});