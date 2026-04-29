package epaw.lab1;

import epaw.lab1.util.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// Recibir parámetros del formulario
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String birthDate = request.getParameter("birthDate");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String dni = request.getParameter("dni");
		String zip = request.getParameter("zip");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String phone = request.getParameter("phone");
		String gender = request.getParameter("gender");
		String password = request.getParameter("password");
		String passwordCheck = request.getParameter("passwordCheck");

		// Validar los datos
		String error = validateRegistration(name, surname, birthDate, email, username, dni, zip, city, country, phone, gender, password, passwordCheck);

		if (error != null) {
			// Si hay error, mostrar página de error
			showErrorPage(out, error);
			return;
		}

		// Validar duplicados en base de datos
		try (DBManager db = new DBManager()) {
			if (db.emailExists(email)) {
				showErrorPage(out, "El correu electrònic ja està registrat.");
				return;
			}

			if (db.usernameExists(username)) {
				showErrorPage(out, "El nom d'usuari ja està en ús.");
				return;
			}

			if (db.dniExists(dni)) {
				showErrorPage(out, "El DNI ja està registrat.");
				return;
			}

			// Insertar usuario en la base de datos
			boolean success = db.insertUser(name, surname, birthDate, email, username, dni, country, city, zip, phone, gender, password);

			if (success) {
				showSuccessPage(out);
			} else {
				showErrorPage(out, "Error al guardar l'usuari. Intenta-ho de nou més tard.");
			}
		} catch (Exception e) {
			showErrorPage(out, "Error de base de dades: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Validar todos los campos según las reglas del Lab 2
	 */
	private String validateRegistration(String name, String surname, String birthDate, String email,
			String username, String dni, String zip, String city, String country, String phone,
			String gender, String password, String passwordCheck) {

		// Validar campos obligatorios
		if (isEmpty(name) || isEmpty(surname) || isEmpty(birthDate) || isEmpty(email) ||
				isEmpty(username) || isEmpty(dni) || isEmpty(zip) || isEmpty(city) || isEmpty(country) ||
				isEmpty(password) || isEmpty(passwordCheck)) {
			return "Tots els camps marcat amb * són obligatoris.";
		}

		// Validar nombre (2-60 caracteres, solo letras, espacios, guiones, apóstrofes)
		if (name.length() < 2 || name.length() > 60) {
			return "El nom ha de tener entre 2 i 60 caràcters.";
		}
		if (!name.matches("[a-zA-Záéíóúàèìòùäëïöüñ\\s'-]+")) {
			return "El nom només pot contenir lletres, espais, guions i apòstrofes.";
		}

		// Validar apellido (2-80 caracteres, solo letras, espacios, guiones, apóstrofes)
		if (surname.length() < 2 || surname.length() > 80) {
			return "Els cognoms han de tener entre 2 i 80 caràcters.";
		}
		if (!surname.matches("[a-zA-Záéíóúàèìòùäëïöüñ\\s'-]+")) {
			return "Els cognoms només poden contenir lletres, espais, guions i apòstrofes.";
		}

		// Validar email
		if (!isValidEmail(email)) {
			return "El correu electrònic no té un format vàlid.";
		}

		// Validar username (4-30 caracteres, solo letras, números, guion bajo)
		if (username.length() < 4 || username.length() > 30) {
			return "El nom d'usuari ha de tener entre 4 i 30 caràcters.";
		}
		if (!username.matches("^[a-zA-Z0-9_]+$")) {
			return "El nom d'usuari només pot contenir lletres, números i guion baix.";
		}

		// Validar DNI (8 dígitos y 1 letra mayúscula - formato español)
		if (!dni.matches("^[0-9]{8}[A-Z]$")) {
			return "El DNI ha de tener el format: 8 dígits i 1 lletra majúscula (ex: 12345678A).";
		}

		// Validar código postal (exactamente 5 dígitos)
		if (!zip.matches("^[0-9]{5}$")) {
			return "El codi postal ha de contenir exactament 5 dígits.";
		}

		// Validar teléfono (opcional, pero si se informa: 7-15 dígitos)
		if (!isEmpty(phone)) {
			if (!phone.matches("^[0-9]{7,15}$")) {
				return "El telèfon ha de contenir entre 7 i 15 dígits.";
			}
		}

		// Validar contraseña (mínimo 8 caracteres, al menos una mayúscula, un número y un carácter especial)
		if (password.length() < 8) {
			return "La contrasenya ha de tener almenys 8 caràcters.";
		}
		if (!password.matches(".*[A-Z].*")) {
			return "La contrasenya ha de contenir almenys una lletra majúscula.";
		}
		if (!password.matches(".*[0-9].*")) {
			return "La contrasenya ha de contenir almenys un número.";
		}
		if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
			return "La contrasenya ha de contenir almenys un caràcter especial (!@#$%^&* etc).";
		}

		// Validar que las contraseñas coincidan
		if (!password.equals(passwordCheck)) {
			return "Les contrasenyes no coincideixen.";
		}

		return null; // Sin errores
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return Pattern.matches(emailRegex, email);
	}

	private void showErrorPage(PrintWriter out, String errorMessage) {
		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"ca\">");
		out.println("<head>");
		out.println("    <meta charset=\"UTF-8\">");
		out.println("    <title>Error de Registre - BubbleNet</title>");
		out.println("    <link rel=\"stylesheet\" href=\"styles.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<main class=\"page\">");
		out.println("    <section class=\"card\">");
		out.println("        <h1>Error de Registre</h1>");
		out.println("        <div class=\"error-message\">");
		out.println("            <p>" + errorMessage + "</p>");
		out.println("        </div>");
		out.println("        <p><a href=\"index.html\" class=\"back-link\">Torna al formulari de registre</a></p>");
		out.println("    </section>");
		out.println("</main>");
		out.println("</body>");
		out.println("</html>");
	}

	private void showSuccessPage(PrintWriter out) {
		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"ca\">");
		out.println("<head>");
		out.println("    <meta charset=\"UTF-8\">");
		out.println("    <title>Registre Exitós - BubbleNet</title>");
		out.println("    <link rel=\"stylesheet\" href=\"styles.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<main class=\"page\">");
		out.println("    <section class=\"card\">");
		out.println("        <h1>¡Registre Completat!</h1>");
		out.println("        <div class=\"success-message\">");
		out.println("            <p>¡Felicitacions! El teu compte s'ha creat correctament.</p>");
		out.println("            <p>Ja pots accedir a BubbleNet amb el teu nom d'usuari i contrasenya.</p>");
		out.println("        </div>");
		out.println("        <p><a href=\"index.html\" class=\"back-link\">Tornar a registre</a></p>");
		out.println("    </section>");
		out.println("</main>");
		out.println("</body>");
		out.println("</html>");
	}
}
