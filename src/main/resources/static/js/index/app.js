import { initCategoryMenu } from './additional/categoryMenu.js';
document.addEventListener('DOMContentLoaded', () => {
    let cart = [];

    const savedCart = localStorage.getItem('cartItems');
    if (savedCart) {
        try {
            cart = JSON.parse(savedCart);
        } catch (e) {
            console.warn('Ошибка при парсинге корзины из localStorage', e);
            cart = [];
        }
    }

    function groupByCategory(items) {
        return items.reduce((acc, currentItem) => {
            const categoryName = currentItem.category;
            acc[categoryName] = [...(acc[categoryName] || []), currentItem];
            return acc;
        }, {});
    }

    function createCategorySection(categoryName, itemsInCategory) {
        const sectionContainer = document.createElement('section');
        sectionContainer.classList.add('category-section');
        sectionContainer.id = categoryName.toLowerCase().replace(/\s+/g, '-');

        const header = document.createElement('h2');
        header.textContent = categoryName;
        sectionContainer.appendChild(header);

        const menuGrid = document.createElement('div');
        menuGrid.classList.add('menu-grid');
        sectionContainer.appendChild(menuGrid);

        itemsInCategory.forEach(item => {
            const itemCard = document.createElement('div');
            itemCard.classList.add('menu-item');
            itemCard.dataset.id = item.id;

            let imageUrl = item.image;

            itemCard.innerHTML = `
                <div class="image-wrapper">
                    <img src="${imageUrl}" alt="${item.name}" class="main-image">
                </div>
                <p class="hidden-description">${item.description || 'Не указано'}</p>
                <div class="item-info">
                    <span class="price">${item.price}</span>
                    <span><strong>₽</strong></span>
                    <span class="item-details"> ${item.heft || 'Не указано'}ккал/${item.pieces || 'Не указано'}шт</span>
                </div>
                <h3 class="item-name">${item.name}</h3>
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
            const response = await fetch('/items');
            if (!response.ok) throw new Error(`Ошибка HTTP (${response.status})`);

            const items = await response.json();
            displayMenu(items);

            document.querySelectorAll('.menu-item').forEach(item => {
                item.addEventListener('click', (event) => {
                    event.preventDefault();

                    const target = event.target.closest('.menu-item');
                    const productData = {
                        id: target.dataset.id,
                        name: target.querySelector('h3').textContent,
                        description: target.querySelector('.hidden-description').textContent,
                        price: parseFloat(target.querySelector('.price').textContent.replace(/[^\d.]/g, '')),
                        image: target.querySelector('.main-image').src,
                        heft: target.querySelector('.item-details')?.textContent.match(/(\d+)ккал/)?.[1] || 'Не указано',
                        pieces: target.querySelector('.item-details')?.textContent.match(/(\d+)шт/)?.[1] || 'Не указано'
                    };

                    showPopup(productData);
                });
            });
        } catch (err) {
            console.error("Ошибка:", err.message);
        }
    }

    function showPopup(data) {
        const popup = document.getElementById('product-popup');
        const overlay = document.getElementById('overlay');
        const content = popup.querySelector('.popup-content');
        const button = content.querySelector('.add-to-cart-from-popup');

        content.querySelector('.name').textContent = data.name;
        content.querySelector('.description').textContent = data.description;
        content.querySelector('.popup-price').textContent =
            `${data.price} ₽${data.heft ? ` / ${data.heft} ккал` : ''}${data.pieces ? ` / ${data.pieces} шт` : ''}`;
        content.querySelector('.popup-image').src = data.image;

        button.dataset.id = data.id;
        button.dataset.name = data.name;
        button.dataset.price = data.price;
        button.dataset.image = data.image;

        button.onclick = () => {
            const itemId = button.dataset.id;
            const itemName = button.dataset.name;
            const itemPrice = parseFloat(button.dataset.price);
            const itemImage = button.dataset.image;

            const idx = cart.findIndex(i => i.id === itemId);
            if (idx !== -1) {
                cart[idx].quantity++;
            } else {
                cart.push({
                    id: itemId,
                    name: itemName,
                    price: itemPrice,
                    quantity: 1,
                    image: itemImage
                });
            }

            localStorage.setItem('cartItems', JSON.stringify(cart));
            updateCart();
            renderCartModal();
            hidePopup();
        };

        popup.classList.remove('hidden');
        overlay.classList.remove('hidden');
    }

    function hidePopup() {
        const popup = document.getElementById('product-popup');
        const overlay = document.getElementById('overlay');
        popup.classList.add('hidden');
        overlay.classList.add('hidden');
    }

    document.querySelector('#product-popup .close-btn').addEventListener('click', hidePopup);

    document.body.addEventListener('click', (event) => {
        if (event.target.matches('#product-popup') || event.target.matches('#overlay')) {
            hidePopup();
        }
    });

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

        if (totalQuantity > 0) {
            document.getElementById('cart-bar').classList.remove('hidden');
        } else {
            document.getElementById('cart-bar').classList.add('hidden');
        }

        localStorage.setItem('cartItems', JSON.stringify(cart));
    }

    function renderCartModal(page = 1) {
        const itemsPerPage = 3;
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
                <div class="cart-item">
                    <div class="cart-item-details">
                        <img src="${item.image}" alt="${item.name}" width="80"/>
                        <div class="item-info">
                            <h3>${item.name}</h3>
                            <p>Цена: ${item.price} ₽</p>
                            <p>Общая цена: ${(item.price * item.quantity).toFixed(2)} ₽</p>
                        </div>
                        <div class="cart-item-quantity-container">
                            <div class="cart-item-quantity-controls">
                                <button class="decrease-btn" data-id="${item.id}">-</button>
                                <span class="cart-item-quantity">${item.quantity}</span>
                                <button class="increase-btn" data-id="${item.id}">+</button>
                            </div>
                        </div>
                    </div>
                </div>
            `;

            cartItemsContainer.appendChild(itemDiv);
        });

        document.querySelectorAll('.increase-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const itemId = btn.getAttribute('data-id');
                const item = cart.find(i => i.id == itemId);
                if (item) {
                    item.quantity++;
                    updateCart();
                    renderCartModal(page);
                }
            });
        });

        document.querySelectorAll('.decrease-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const itemId = btn.getAttribute('data-id');
                const itemIndex = cart.findIndex(i => i.id == itemId);

                if (itemIndex !== -1) {
                    if (cart[itemIndex].quantity > 1) {
                        cart[itemIndex].quantity--;
                    } else {
                        cart.splice(itemIndex, 1);
                    }
                    updateCart();
                    const newTotalPages = Math.ceil(cart.length / itemsPerPage);
                    const newPage = page > newTotalPages ? Math.max(1, newTotalPages) : page;
                    renderCartModal(newPage);
                }
            });
        });

        cartCountItems.textContent = `${totalQuantity}`;
        cartTotalSum.textContent = `${totalPrice.toFixed(2)} ₽`;
        localStorage.setItem('cartTotalSum', cartTotalSum.textContent);

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

    updateCart();
    renderCartModal();

    initCategoryMenu();
    fetchItems();
});