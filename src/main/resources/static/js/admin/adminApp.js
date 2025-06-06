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
            hideNotification();
        }, 3000);
    }

    function hideNotification() {
        const notification = document.getElementById('notification');
        notification.classList.remove('show');
    }

    document.getElementById('create-form').addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(this);
        const submitBtn = this.querySelector('button[type="submit"]');
        const originalBtnText = submitBtn.innerHTML;

        try {
            submitBtn.innerHTML = '<span>Сохранение...</span>';
            submitBtn.disabled = true;

            const response = await fetch('/admin/items', {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                const message = await response.text();
                submitBtn.innerHTML = originalBtnText;
                            submitBtn.disabled = false;
                showNotification('Успех!', message);
                this.reset();
                document.getElementById('preview').style.display = 'none';
                document.querySelector('.upload-btn span').textContent = 'Выберите файл или перетащите его сюда';
            } else {
                const error = await response.text();
                showNotification('Ошибка!', error || 'Ошибка при создании товара', true);
            }
        } catch (error) {
            showNotification('Ошибка!', 'Ошибка сети: ' + error.message, true);
        }
    });

    document.getElementById('delete-form').addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(this);
        const submitBtn = this.querySelector('button[type="submit"]');
        const originalBtnText = submitBtn.innerHTML;

        try {
            submitBtn.innerHTML = '<span>Удаление...</span>';
            submitBtn.disabled = true;

            const response = await fetch('/admin/items', {
                method: 'DELETE',
                body: formData
            });

            if (response.ok) {
                const message = await response.text();
                submitBtn.innerHTML = originalBtnText;
                            submitBtn.disabled = false;
                showNotification('Успех!', message);
                this.reset();
            } else {
                const error = await response.text();
                submitBtn.innerHTML = originalBtnText;
                            submitBtn.disabled = false;
                showNotification('Ошибка!', error || 'Ошибка при удалении товара', true);
            }
        } catch (error) {
            showNotification('Ошибка!', 'Ошибка сети: ' + error.message, true);
        }
    });

    document.getElementById('save-bot-btn').addEventListener('click', async function(e) {
            e.preventDefault();

            const botName = document.getElementById('bot-name').value;
            const botToken = document.getElementById('bot-token').value;

            try {
                const submitBtn = this;
                const originalBtnText = submitBtn.innerHTML;

                submitBtn.innerHTML = '<span>Сохранение...</span>';
                submitBtn.disabled = true;

                const response = await fetch('/items/save-options/bot', {
                    method: 'POST',
                    body: new URLSearchParams({
                        botName: botName,
                        botToken: botToken
                    })
                });

                if (response.ok) {
                    const message = await response.text();
                    submitBtn.innerHTML = originalBtnText;
                                    submitBtn.disabled = false;
                    showNotification('Успех!', message);
                } else {
                    const error = await response.text();
                    submitBtn.innerHTML = originalBtnText;
                                    submitBtn.disabled = false;
                    showNotification('Ошибка!', error || 'Ошибка при сохранении данных', true);
                }
            } catch (error) {
                showNotification('Ошибка!', 'Ошибка сети: ' + error.message, true);
            }
        });

        document.getElementById('save-option-widget-btn').addEventListener('click', function(e) {
            e.preventDefault();

            const widgetName = document.getElementById('name-widget').value.trim();
            if (!widgetName) {
                showNotification('Ошибка!', 'Введите название виджета', true);
                return;
            }

            localStorage.setItem('widgetName', widgetName);
            showNotification('Успех!', 'Название виджета сохранено');

            updateToggleButton(widgetName);
        });

        function updateToggleButton(name) {
            const toggleBtn = document.getElementById('toggle-btn');
            if (toggleBtn) {
                toggleBtn.innerHTML = `
                    <span>${name}</span>
                    <i class="fas fa-chevron-down" id="menu-icon"></i>
                `;
            }
        }

        document.getElementById('set-bank-form').addEventListener('submit', async function(e) {
                e.preventDefault();

                const formData = new FormData(this);
                const submitBtn = this.querySelector('button[type="submit"]');
                const originalBtnText = submitBtn.innerHTML;
                const idName = document.getElementById('id-name').value;

                try {
                    submitBtn.innerHTML = '<span>Сохранение...</span>';
                    submitBtn.disabled = true;

                    localStorage.setItem('idName', idName);
                    console.log(localStorage.getItem('idName') || "gg");

                    const response = await fetch('/items/set-bank', {
                        method: 'POST',
                        body: formData
                    });

                    if (response.ok) {
                        const message = await response.text();
                        submitBtn.innerHTML = originalBtnText;
                        submitBtn.disabled = false;
                        showNotification('Успех!', message);
                        this.reset();
                    } else {
                        const error = await response.text();
                        submitBtn.innerHTML = originalBtnText;
                        submitBtn.disabled = false;
                        showNotification('Ошибка!', error || 'Ошибка при изменении данных банка', true);
                    }
                } catch (error) {
                    submitBtn.innerHTML = originalBtnText;
                    submitBtn.disabled = false;
                    showNotification('Ошибка!', 'Ошибка сети: ' + error.message, true);
                }
            });

});