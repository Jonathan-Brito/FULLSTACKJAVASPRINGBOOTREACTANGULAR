package br.com.brito.bff.business;

import br.com.brito.bff.infrastructure.entity.Usuario;
import br.com.brito.bff.infrastructure.exceptions.ConflictException;
import br.com.brito.bff.infrastructure.exceptions.ResourceNotFoundException;
import br.com.brito.bff.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Método responsável por salvar usuario
    public Usuario salvarUsuario(Usuario usuario){
        try {
            emailExiste(usuario.getEmail()); // Verifica se email já existe
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não cadastrado" + email));
    }

    public void deleteUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
