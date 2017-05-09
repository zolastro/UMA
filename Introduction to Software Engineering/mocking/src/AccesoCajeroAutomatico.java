import org.junit.Before;
import org.junit.Test;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccesoCajeroAutomatico {
	IRepositorioDeInformacionDeAcceso repository;
	private States state = States.IDLE;


	public void setRepository(IRepositorioDeInformacionDeAcceso repository) {
		this.repository = repository;
	}

	public boolean acceso(String user, String passwd) throws ATMException {
		if (state == States.CHECKING) {
			throw new ATMException();
		}

		state = States.CHECKING;

		IInformaciónDeAcceso info;
		try {
			info = repository.buscar(user);
		} catch (ATMException e) {
			return false;
		}

		boolean success = info.checkPassword(passwd);

		state = States.IDLE;
		return success;
	}

	@Test
	public void correctAccessTest() throws ATMException {
		// Inicializar
		IRepositorioDeInformacionDeAcceso repo = mock(IRepositorioDeInformacionDeAcceso.class);
		IInformaciónDeAcceso existingUser = mock(IInformaciónDeAcceso.class);

		// Stubbing
		when(repo.buscar("Pepe")).thenReturn(existingUser);
		when(existingUser.checkPassword("1234")).thenReturn(true);
		// Ejecutar
		this.setRepository(repo);
		assertTrue(acceso("Pepe", "1234"));
	}
	
	@Test
	public void nonExistingUser() throws ATMException {
		// Inicializar
		IRepositorioDeInformacionDeAcceso repo = mock(IRepositorioDeInformacionDeAcceso.class);

		// Stubbing
		when(repo.buscar("Unknown")).thenThrow(new ATMException("Baia"));
		// Ejecutar
		this.setRepository(repo);
		assertFalse(acceso("Unknown", "1234"));
	}
	
	@Test
	public void wrongPassword() throws ATMException {
		// Inicializar
		IRepositorioDeInformacionDeAcceso repo = mock(IRepositorioDeInformacionDeAcceso.class);
		IInformaciónDeAcceso existingUser = mock(IInformaciónDeAcceso.class);

		// Stubbing
		when(repo.buscar("Pepe")).thenReturn(existingUser);
		when(existingUser.checkPassword("1234")).thenReturn(false);

		// Ejecutar
		this.setRepository(repo);
		assertFalse(acceso("Pepe", "1234"));
	}

}

enum States {
	CHECKING, IDLE
};