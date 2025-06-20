package br.com.brito.bff.business;

import br.com.brito.bff.infrastructure.entity.Usuario;
import br.com.brito.bff.infrastructure.exceptions.ConflictException;
import br.com.brito.bff.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Método responsável por salvar usuario
    public Usuario salvarUsuario(Usuario usuario){
        try {
            emailExiste(usuario.getEmail()); // Verifica se email já existe
            return usuarioRepository.save(usuario);
        } catch (ConflictException e){
            throw new ConflictException("Email já cadastrado", e.getCause());
        }

    }

    // Verifica se email existe
    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado" + email);
            }
        } catch (ConflictException e) {
            throw new RuntimeException("Email já cadastrado", e.getCause());
        }
    }
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }
}
