package br.org.sesisenai.clinipet.security.Users;

import br.org.sesisenai.clinipet.model.entity.Pessoa;
import br.org.sesisenai.clinipet.security.Tipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJpa implements UserDetails {

    private Pessoa pessoa;

    public List<Tipo> authorities;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    public String password;

    public String username;

    public UserJpa(Pessoa pessoa){
        this.pessoa = pessoa;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
        this.password = pessoa.getSenha();
        this.username = pessoa.getEmail();
        this.authorities = new ArrayList<>();
        this.authorities.add(Tipo.tipoOf(pessoa.getClass().getSimpleName()));
    }
}
