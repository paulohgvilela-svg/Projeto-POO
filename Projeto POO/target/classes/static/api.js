const API_URL = 'http://localhost:8080';

// Armazena a lista de Proprietários para lookup rápido do nome
let proprietariosMap = {};

// --- MÓDULO HTTP CLIENT ---
const HttpClient = {
    get: (path) => fetch(`${API_URL}/${path}`).then(res => res.json()),
    post: (path, data) => fetch(`${API_URL}/${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    }),
    del: (path) => fetch(`${API_URL}/${path}`, { method: 'DELETE' })
};


// -----------------------------------------------------------------------
// --- FUNÇÕES DE PROPRIETÁRIO ---
// -----------------------------------------------------------------------

async function fetchProprietarios() {
    const response = await HttpClient.get('proprietarios');
    proprietariosMap = response.reduce((map, p) => {
        map[p.id] = p.nome;
        return map;
    }, {});
    renderProprietarios(response);
}

document.getElementById('proprietario-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('p-nome').value;
    const contato = document.getElementById('p-contato').value;

    // ATUALIZADO: Captura os novos dados
    const endereco = document.getElementById('p-endereco').value;
    const cpf = document.getElementById('p-cpf').value;

    const response = await HttpClient.post('proprietarios', {
        nome,
        contato,
        endereco, // Envia Endereço
        cpf       // Envia CPF
    });

    if (response.status === 201) {
        alert('Proprietário cadastrado com sucesso!');
        document.getElementById('proprietario-form').reset();
        await fetchProprietarios();
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


// -----------------------------------------------------------------------
// --- FUNÇÕES DE FERRAMENTA ---
// -----------------------------------------------------------------------

async function fetchFerramentas() {
    const ferramentas = await HttpClient.get('ferramentas');
    renderFerramentas(ferramentas);
}

document.getElementById('ferramenta-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('f-nome').value;
    const condicao = document.getElementById('f-condicao').value;
    const proprietarioId = document.getElementById('f-proprietario-id').value;

    // ATUALIZADO: Captura o novo campo 'marca'
    const marca = document.getElementById('f-marca').value;

    const response = await HttpClient.post('ferramentas', {
        nome,
        condicao,
        marca, // Envia Marca
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

        // Exibe o nome do Proprietário
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


// -----------------------------------------------------------------------
// --- INICIALIZAÇÃO ---
// -----------------------------------------------------------------------
document.addEventListener('DOMContentLoaded', async () => {
    await fetchProprietarios();
    fetchFerramentas();
});