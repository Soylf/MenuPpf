document.addEventListener('DOMContentLoaded', () => {
    function showNotification(title, message, isError = false) {
        const notification = document.getElementById('notification');
        const notificationTitle = document.getElementById('notification-title');
        const notificationMessage = document.getElementById('notification-message');
        const notificationIcon = notification.querySelector('.notification-icon');

        notificationTitle.textContent = title;
        notificationMessage.textContent = message;
        notificationIcon.textContent = isError ? '✕' : '✓';

        notification.className = isError ? 'notification error' : 'notification success';

        notification.classList.add('show');

        setTimeout(() => {
            notification.classList.remove('show');
        }, 3000);
    }

    const contactButton = document.querySelector('.btn-contact');
    const form = document.getElementById('form');
    const payButton = document.querySelector('.btn-pay');

    payButton.addEventListener('click', async (e) => {
        e.preventDefault();

        const cartTotalSum = localStorage.getItem('cartTotalSum');
        const idName = localStorage.getItem('idName');

        if (!cartTotalSum || !idName) {
            showNotification('Ошибка!', 'Недостаточно данных для оплаты.', true);
            return;
        }

        const userName = form.name.value.trim();
        const age = form.age.value.trim();
        const relations = form.querySelector('input[name="connection"]:checked').value;
        const comment = form.comment.value.trim();

        if (!userName) {
            showNotification('Ошибка!', 'Пожалуйста, укажите ваше имя.', true);
            return;
        }

        try {
            const params = new URLSearchParams({
                sum: cartTotalSum,
                name: idName,
                userName: userName,
                age: age,
                relations: relations,
                comment: comment
            });

            const response = await fetch(`/order/create?${params.toString()}`, {
                method: 'POST'
            });

            if (!response.ok) {
                throw new Error("Ошибка при создании заказа.");
            }

            const qrLink = await response.text();

            // Перенаправление на страницу с QR-кодом
            window.location.href = qrLink;

        } catch (error) {
            showNotification('Ошибка!', 'Произошла ошибка при попытке оплаты: ' + error.message, true);
        }
    });

    contactButton.addEventListener('click', async () => {
        const userName = form.name.value.trim();
        const age = form.age.value.trim();
        const relations = form.querySelector('input[name="connection"]:checked').value;
        const comment = form.comment.value.trim();

        if (!userName) {
            showNotification('Ошибка!', 'Пожалуйста, укажите ваше имя.', true);
            return;
        }

        try {
            const params = new URLSearchParams({
                userName: userName,
                age: age,
                relations: relations,
                comment: comment
            });

            const response = await fetch(`/order/relations?${params.toString()}`, {
                method: 'POST'
            });

            if (!response.ok) {
                throw new Error("Ошибка при отправке запроса на связь.");
            }

            showNotification('Успех!', 'Ваши данные с формы отправленный! ヽ(*・ω・)ﾉ');

        } catch (error) {
            showNotification('Ошибка!', 'Произошла ошибка при попытке отправить запрос: ' + error.message, true);
        }
    });
});