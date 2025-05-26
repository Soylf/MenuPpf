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
        const itemDtos = localStorage.getItem("cartItems")

        if (!cartTotalSum || !idName) {
            showNotification('Ошибка!', 'Недостаточно данных для оплаты.', true);
            return;
        }

        const userName = form.name.value.trim();
        const age = form.age.value.trim();
        const comment = form.comment.value.trim();
        const contact = form['contact-info'].value.trim();

        if (!userName) {
            showNotification('Ошибка!', 'Пожалуйста, укажите ваше имя.', true);
            return;
        }

        try {
            const bodyData = {
                sum: parseInt(cartTotalSum),
                name: idName,
                userName: userName,
                age: age,
                relations: contact,
                comment: comment,
                itemDtos: JSON.parse(itemDtos)
            };

            const response = await fetch('/order/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bodyData)
            });

            if (!response.ok) {
                throw new Error('Ошибка при создании заказа.');
            }

            const qrLink = await response.text();
            window.location.href = qrLink;

        } catch (error) {
            showNotification('Ошибка!', 'Произошла ошибка при попытке оплаты: ' + error.message, true);
        }
    });

    contactButton.addEventListener('click', async () => {
        const userName = form.name.value.trim();
        const age = form.age.value.trim();
        const contact = form['contact-info'].value.trim();
        const comment = form.comment.value.trim();
        const itemDtos = localStorage.getItem("cartItems")
        const cartTotalSum = localStorage.getItem('cartTotalSum');

        if (!userName) {
            showNotification('Ошибка!', 'Пожалуйста, укажите ваше имя.', true);
            return;
        }

        try {
            const bodyData = {
                sum: parseInt(cartTotalSum),
                userName: userName,
                age: age,
                relations: contact,
                comment: comment,
                itemDtos: JSON.parse(itemDtos)
            };

            const response = await fetch('/order/relations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bodyData)
            });

            if (!response.ok) {
                throw new Error('Ошибка при отправке запроса на связь.');
            }

            showNotification('Успех!', 'Ваши данные с формы отправлены! ヽ(*・ω・)ﾉ');

        } catch (error) {
            showNotification('Ошибка!', 'Произошла ошибка при попытке отправить запрос: ' + error.message, true);
        }
    });
});