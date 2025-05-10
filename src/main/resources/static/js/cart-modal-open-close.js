document.addEventListener('DOMContentLoaded', () => {
    const cartBar = document.getElementById('cart-bar');
    const cartBackdrop = document.getElementById('cart-backdrop');
    const cartModal = document.getElementById('cart-modal');
    const closeCartBtn = document.getElementById('close-cart');

    cartBar.addEventListener('click', () => {
        cartBackdrop.classList.remove('hidden');
        cartModal.classList.remove('hidden');
    });

    closeCartBtn.addEventListener('click', () => {
        cartBackdrop.classList.add('hidden');
        cartModal.classList.add('hidden');
    });

    cartBackdrop.addEventListener('click', () => {
        cartBackdrop.classList.add('hidden');
        cartModal.classList.add('hidden');
    });
});
