document.addEventListener("DOMContentLoaded", () => {
    const formulario = document.getElementById("formCarona");

    if (!formulario) return;

    const botao = formulario.querySelector('button[type="submit"]');

    const grupoMotorista = document.createElement("div");
    grupoMotorista.innerHTML = `
        <label for="motoristaId">Motorista responsável</label>
        <select id="motoristaId" required>
            <option value="">Carregando usuários...</option>
        </select>
    `;

    formulario.insertBefore(grupoMotorista, botao);

    const motoristaSelect = document.getElementById("motoristaId");

    fetch("/api/usuarios")
        .then(response => {
            if (!response.ok) {
                throw new Error("Não foi possível carregar os usuários.");
            }
            return response.json();
        })
        .then(usuarios => {
            motoristaSelect.innerHTML =
                '<option value="">Selecione o motorista</option>';

            usuarios.forEach(usuario => {
                const opcao = document.createElement("option");
                opcao.value = usuario.id;
                opcao.textContent = usuario.nome;
                motoristaSelect.appendChild(opcao);
            });

            if (usuarios.length === 0) {
                motoristaSelect.innerHTML =
                    '<option value="">Cadastre um usuário primeiro</option>';
            }
        })
        .catch(erro => {
            motoristaSelect.innerHTML =
                '<option value="">Erro ao carregar usuários</option>';
            alert(erro.message);
        });

    formulario.addEventListener("submit", async (evento) => {
        evento.preventDefault();

        const motoristaId = motoristaSelect.value;

        if (!motoristaId) {
            alert("Selecione o motorista responsável.");
            return;
        }

        const carona = {
            origem: document.getElementById("origem").value.trim(),
            destino: document.getElementById("destino").value.trim(),
            data: document.getElementById("data").value,
            horario: document.getElementById("horario").value,
            vagasDisponiveis: Number(
                document.getElementById("vagas").value
            )
        };

        botao.disabled = true;
        botao.textContent = "Publicando...";

        try {
            const resposta = await fetch(
                `/api/caronas/${motoristaId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(carona)
                }
            );

            if (!resposta.ok) {
                const mensagem = await resposta.text();
                throw new Error(mensagem || "Não foi possível publicar a carona.");
            }

            alert("Carona publicada com sucesso!");
            formulario.reset();

        } catch (erro) {
            alert("Erro ao publicar carona: " + erro.message);

        } finally {
            botao.disabled = false;
            botao.textContent = "Publicar carona";
        }
    });
});
