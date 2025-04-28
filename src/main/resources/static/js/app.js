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

          <button type="button" class="add-to-cart-btn" data-id="${item.id}" data-name="${item.name}" data-price="${item.price}" data-image="${item.image}">
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

  function displayMenu(items) {
    const groupedItems = groupByCategory(items);
    const container = document.getElementById('menu-container');
    container.innerHTML = '';

    Object.keys(groupedItems).forEach(category => {
      const categorySection = createCategorySection(category, groupedItems[category]);
      container.appendChild(categorySection);
    });
  }

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
               const itemImage = button.getAttribute('data-image');

               const existingItemIndex = cart.findIndex(item => item.id === itemId);

                if (existingItemIndex !== -1) {
                  cart[existingItemIndex].quantity++;
                } else {
                     cart.push({
                        id: itemId,
                        name: itemName,
                        price: itemPrice,
                        quantity: 1,
                        image: itemImage
                     });
                }
                updateCart();
                renderCartModal();
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

      function renderCartModal(page = 1) {
          const itemsPerPage = 4;
          const startIndex = (page - 1) * itemsPerPage;
          const endIndex = startIndex + itemsPerPage;
          const paginatedItems = cart.slice(startIndex, endIndex);

          const cartItemsContainer = document.getElementById('cart-items-container');
          const cartCountItems = document.getElementById('cart-count-items');
          const cartTotalSum = document.getElementById('cart-total-sum');
          const paginationContainer = document.getElementById('pagination');

          cartItemsContainer.innerHTML = '';
          paginationContainer.innerHTML = '';

          let totalQuantity = 0;
          let totalPrice = 0;

          cart.forEach(item => {
              totalQuantity += item.quantity;
              totalPrice += item.price * item.quantity;
          });

          paginatedItems.forEach(item => {
              const itemDiv = document.createElement('div');
              itemDiv.classList.add('cart-item');

              itemDiv.innerHTML = `
                  <div class="cart-item-details">
                      <img src="${item.image}" alt="${item.name}" width="80"/>
                      <div class="item-info">
                          <h3>${item.name} × ${item.quantity}</h3>
                          <p>Цена: ${item.price} ₽</p>
                          <p>Общая цена: ${item.price * item.quantity} ₽</p>
                      </div>
                  </div>
              `;

              cartItemsContainer.appendChild(itemDiv);
          });

          cartCountItems.textContent = `${totalQuantity}`;
          cartTotalSum.textContent = `${totalPrice.toFixed(2)} ₽`;

          const totalPages = Math.ceil(cart.length / itemsPerPage);

          for (let i = 1; i <= totalPages; i++) {
              const pageButton = document.createElement('button');
              pageButton.textContent = i;
              if (i === page) {
                  pageButton.classList.add('active');
              }

              pageButton.addEventListener('click', () => {
                  renderCartModal(i);
              });

              paginationContainer.appendChild(pageButton);
          }
      }


    fetchItems();
});