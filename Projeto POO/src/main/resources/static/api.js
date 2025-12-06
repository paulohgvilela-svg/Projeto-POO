const API_URL = 'http://localhost:8080';

let proprietariosMap = {};

const HttpClient = {
    get: (path) => fetch(`${API_URL}/${path}`).then(res => res.json()),
    post: (path, data) => fetch(`${API_URL}/${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    }),
    del: (path) => fetch(`${API_URL}/${path}`, { method: 'DELETE' })
};


async function fetchProprietarios() {
    const proprietarios = await HttpClient.get('proprietarios');

    proprietariosMap = proprietarios.reduce((map, p) => {
        map[p.id] = p.nome;
        return map;
    }, {});

    renderProprietarios(proprietarios);
}

document.getElementById('proprietario-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('p-nome').value;
    const contato = document.getElementById('p-contato').value;

    const response = await HttpClient.post('proprietarios', { nome, contato });

    if (response.status === 201) {
        alert('Proprietário cadastrado com sucesso!');
        document.getElementById('proprietario-form').reset();
        await fetchProprietarios(); // Atualiza a lista e o mapa
    } else {
        const error = await response.json();
        alert('Erro ao cadastrar proprietário: ' + JSON.stringify(error.errors || error.message));
    }
});

function renderProprietarios(proprietarios) {
    const tbody = document.querySelector('#proprietarios-list tbody');
    tbody.innerHTML = '';
    proprietarios.forEach(p => {
        const row = tbody.insertRow();
        row.insertCell().textContent = p.id;
        row.insertCell().textContent = p.nome;
        row.insertCell().textContent = p.contato;
        row.insertCell().innerHTML = `<button onclick="deleteProprietario(${p.id})">Excluir</button>`;
    });
}

async function deleteProprietario(id) {
    if (!confirm('Tem certeza que deseja excluir este proprietário?')) return;

    const response = await HttpClient.del(`proprietarios/${id}`);

    if (response.status === 204) {
        alert('Proprietário excluído com sucesso!');
        await fetchProprietarios();
        fetchFerramentas();
    } else {
        alert('Erro ao excluir proprietário.');
    }
}


async function fetchFerramentas() {
    const ferramentas = await HttpClient.get('ferramentas');
    renderFerramentas(ferramentas);
}

document.getElementById('ferramenta-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('f-nome').value;
    const condicao = document.getElementById('f-condicao').value;
    const proprietarioId = document.getElementById('f-proprietario-id').value;

    const response = await HttpClient.post('ferramentas', {
        nome,
        condicao,
        proprietario: { id: proprietarioId }
    });

    if (response.status === 201) {
        alert('Ferramenta cadastrada com sucesso!');
        document.getElementById('ferramenta-form').reset();
        fetchFerramentas();
    } else {
        const error = await response.json();
        alert('Erro ao cadastrar ferramenta: ' + JSON.stringify(error.errors || error.message));
    }
});

async function toggleStatus(id, action) {
    const endpoint = `ferramentas/${id}/${action}`;
    const response = await HttpClient.post(endpoint, {});

    if (response.status === 200) {
        fetchFerramentas();
    } else {
        alert('Erro ao mudar status da ferramenta.');
    }
}

function renderFerramentas(ferramentas) {
    const tbody = document.querySelector('#ferramentas-list tbody');
    tbody.innerHTML = '';

    ferramentas.forEach(f => {
        const row = tbody.insertRow();
        row.insertCell().textContent = f.id;
        row.insertCell().textContent = f.nome;
        row.insertCell().textContent = f.status;

        const proprietarioId = f.proprietario ? f.proprietario.id : null;
        const proprietarioNome = proprietariosMap[proprietarioId] || `ID ${proprietarioId} (Não Encontrado)`;

        row.insertCell().textContent = proprietarioNome;

        const actionsCell = row.insertCell();
        if (f.status === 'disponivel') {
            actionsCell.innerHTML = `<button onclick="toggleStatus(${f.id}, 'emprestar')">Emprestar</button>`;
        } else {
            actionsCell.innerHTML = `<button onclick="toggleStatus(${f.id}, 'devolver')">Devolver</button>`;
        }
    });
}

document.addEventListener('DOMContentLoaded', async () => {
    await fetchProprietarios();
    fetchFerramentas();
});