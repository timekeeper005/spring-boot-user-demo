const form = document.getElementById('registerForm');
const resultBox = document.getElementById('resultBox');

function showResult(type, message) {
    resultBox.classList.remove('hidden', 'success', 'error');
    resultBox.classList.add(type);
    resultBox.textContent = message;
}

form.addEventListener('submit', async function (event) {
    event.preventDefault();

    const formData = {
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value,
        name: document.getElementById('name').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: document.getElementById('phone').value.trim()
    };

    try {
        const response = await fetch('/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (response.ok) {
            showResult('success', `注册成功：${result.message}`);
        } else {
            showResult('error', `注册失败：${result.message || '未知错误'}`);
        }
    } catch (error) {
        showResult('error', `请求失败：${error.message}`);
    }
});
