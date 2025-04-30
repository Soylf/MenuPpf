document.addEventListener('DOMContentLoaded', () => {
    const cartBar = document.getElementById('cart-bar');
    const cartBackdrop = document.getElementById('cart-backdrop');
    const cartModal = document.getElementById('cart-modal');
    const closeCartBtn = document.getElementById('close-cart');

    cartBar.addEventListener('click', () => {
        cartBackdrop.classList.add('show');
        cartModal.classList.add('show');
    });

    closeCartBtn.addEventListener('click', () => {
        cartBackdrop.classList.remove('show');
        cartModal.classList.remove('show');
    });

    cartBackdrop.addEventListener('click', () => {
        cartBackdrop.classList.remove('show');
        cartModal.classList.remove('show');
    });
});