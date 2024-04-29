# PA Secure Chat

## Introdução
Este projeto, desenvolvido no contexto da disciplina de Programação Avançada, é focado na criação de um sistema de mensagens instantâneas seguro, denominado `pa-secure-chat`. O sistema visa permitir a comunicação em tempo real entre os utilizadores com garantias de privacidade, integridade e autenticidade das mensagens.

## Funcionalidades
- **Comunicação Segura:** Utilização de encriptação simétrica e assimétrica para garantir a confidencialidade das mensagens.
- **Autenticação e Não Repúdio:** Implementação de MAC, hash, e mecanismos de geração de chaves para assegurar a autenticidade e integridade das mensagens.
- **Gestão de Certificados:** Uso de uma Autoridade de Certificação para emitir e validar certificados digitais dos utilizadores.

## Arquitetura
- O sistema é composto por um servidor central que encaminha as mensagens entre os usuários sem armazenar ou ter capacidade para descriptografar as mensagens.
- Os usuários geram um par de chaves (pública e privada) e obtêm um certificado digital da CA para autenticar suas chaves públicas.

## Requisitos
- Java 11 ou superior
- Bibliotecas de criptografia para Java
- Servidor e clientes devem ter capacidade de realizar operações de criptografia e de rede

## Configuração
Todas as configurações necessárias para o funcionamento do projeto devem ser especificadas no arquivo `project.config`.

## Como Usar
1. **Iniciar o Servidor:** Executar o script de inicialização do servidor.
2. **Conectar Cliente:** Os clientes devem conectar-se ao servidor utilizando um identificador único.
3. **Comunicação:** Após a autenticação via certificado digital, os usuários podem enviar mensagens usando a sintaxe `@NomeUtilizador <mensagem>` para comunicação privada ou simplesmente digitar a mensagem para envio em broadcast.

## Testes e Documentação
- Testes de integração devem ser realizados utilizando a biblioteca JaCoCo para relatório de code coverage.
- Documentação deve ser gerada utilizando Javadoc e armazenada no repositório.

## Contribuições
As contribuições para este projeto são geridas através de pull requests no GitHub. Cada contribuição será avaliada individualmente para garantir que cumpre os requisitos de segurança e funcionalidade.

## Código de Ética
Espera-se que todos os contribuidores sigam os mais altos padrões de honestidade acadêmica. Qualquer ideia ou código que não seja original do aluno deve ser adequadamente citado.

## Autores
- João Pedro Abreu 2081421
- Carlos Coelho 2078221
- Rircado Vieira 2126921

