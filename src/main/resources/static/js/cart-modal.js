document.addEventListener('DOMContentLoaded', () => {
    const cartBar = document.getElementById('cart-bar');
    const cartBackdrop = document.getElementById('cart-backdrop');
    const cartModal = document.getElementById('cart-modal');
    const closeCartBtn = document.getElementById('close-cart');

    // Обработчик события для открытия корзины
    cartBar.addEventListener('click', () => {
        cartBackdrop.classList.add('show');
        cartModal.classList.add('show');
    });

    // Обработчик события для закрытия корзины
    closeCartBtn.addEventListener('click', () => {
        cartBackdrop.classList.remove('show');
        cartModal.classList.remove('show');
    });

    // Обработчик события для закрытия корзины при клике на backdrop
    cartBackdrop.addEventListener('click', () => {
        cartBackdrop.classList.remove('show');
        cartModal.classList.remove('show');
    });
});