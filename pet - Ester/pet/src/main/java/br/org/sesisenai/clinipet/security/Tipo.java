package br.org.sesisenai.clinipet.security;

import org.springframework.security.core.GrantedAuthority;

public enum Tipo  implements GrantedAuthority {

    VETERINARIO("Veterinario"),
    ATENDENTE("Atendente"),
    CLIENTE("Cliente");

    private String descricao;

    Tipo(String descricao) {
        this.descricao = descricao;
    }

    public static Tipo tipoOf(String simpleName) {
        return switch (simpleName) {
            case "Veterinario" -> VETERINARIO;
            case "Atendente" -> ATENDENTE;
            case "Cliente" -> CLIENTE;
            default -> null;
        };
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

}
