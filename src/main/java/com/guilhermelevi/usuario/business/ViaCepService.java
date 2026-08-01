package com.guilhermelevi.usuario.business;

import com.guilhermelevi.usuario.infrastructure.client.IViaCepClient;
import com.guilhermelevi.usuario.infrastructure.client.ViaCepDTO;
import com.guilhermelevi.usuario.infrastructure.exceptions.IllegalArgumentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final IViaCepClient client;

    public ViaCepDTO buscarDadosEndereco(String cep) {
        return client.buscaDadosEndereco(cep);
    }

    private String processaCep(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if (!cepFormatado.matches("\\d+") || !Objects.equals(cepFormatado.length() , 8)) {
            throw new IllegalArgumentsException("O cep contém caracteres inválidos");
        }

        return cepFormatado;

    }

}
