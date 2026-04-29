/**
 * register.js - Validación de formulario de registro en cliente
 * Realiza validaciones inmediatas en los campos del formulario
 */

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const clientMessage = document.getElementById('clientMessage');

    // Adjuntar listeners a todos los campos
    const fields = ['name', 'surname', 'birthDate', 'email', 'username', 'dni', 'zip', 'city', 'country', 'phone', 'password', 'passwordCheck'];

    fields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('blur', function() {
                validateField(fieldId);
            });

            field.addEventListener('input', function() {
                validateField(fieldId);
            });
        }
    });

    // Validar al enviar el formulario
    form.addEventListener('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault();
            showClientMessage('Por favor, corrige los errores en el formulario.', 'error');
        }
    });

    /**
     * Validar un campo específico
     */
    function validateField(fieldId) {
        const field = document.getElementById(fieldId);
        const errorElement = document.getElementById(fieldId + 'Error');
        let error = '';

        switch (fieldId) {
            case 'name':
                error = validateName(field.value);
                break;
            case 'surname':
                error = validateSurname(field.value);
                break;
            case 'birthDate':
                error = validateBirthDate(field.value);
                break;
            case 'email':
                error = validateEmail(field.value);
                break;
            case 'username':
                error = validateUsername(field.value);
                break;
            case 'dni':
                error = validateDNI(field.value);
                break;
            case 'zip':
                error = validateZip(field.value);
                break;
            case 'city':
                error = validateCity(field.value);
                break;
            case 'country':
                error = validateCountry(field.value);
                break;
            case 'phone':
                error = validatePhone(field.value);
                break;
            case 'password':
                error = validatePassword(field.value);
                break;
            case 'passwordCheck':
                error = validatePasswordCheck(field.value);
                break;
        }

        // Mostrar o ocultar error
        if (error) {
            errorElement.textContent = error;
            field.classList.add('invalid');
        } else {
            errorElement.textContent = '';
            field.classList.remove('invalid');
        }
    }

    /**
     * Validar todo el formulario
     */
    function validateForm() {
        let isValid = true;

        fields.forEach(fieldId => {
            validateField(fieldId);
            const field = document.getElementById(fieldId);
            if (field.classList.contains('invalid')) {
                isValid = false;
            }
        });

        return isValid;
    }

    // Funciones de validación específicas
    function validateName(value) {
        if (!value || value.trim().length === 0) {
            return 'El nom és obligatori.';
        }
        if (value.length < 2 || value.length > 60) {
            return 'El nom ha de tener entre 2 i 60 caràcters.';
        }
        if (!/^[a-zA-Záéíóúàèìòùäëïöüñ\s'-]+$/.test(value)) {
            return 'El nom només pot contenir lletres, espais, guions i apòstrofes.';
        }
        return '';
    }

    function validateSurname(value) {
        if (!value || value.trim().length === 0) {
            return 'Els cognoms són obligatoris.';
        }
        if (value.length < 2 || value.length > 80) {
            return 'Els cognoms han de tener entre 2 i 80 caràcters.';
        }
        if (!/^[a-zA-Záéíóúàèìòùäëïöüñ\s'-]+$/.test(value)) {
            return 'Els cognoms només poden contenir lletres, espais, guions i apòstrofes.';
        }
        return '';
    }

    function validateBirthDate(value) {
        if (!value || value.trim().length === 0) {
            return 'La data de naixement és obligatoria.';
        }
        return '';
    }

    function validateEmail(value) {
        if (!value || value.trim().length === 0) {
            return 'El correu electrònic és obligatori.';
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            return 'El correu electrònic no té un format vàlid.';
        }
        return '';
    }

    function validateUsername(value) {
        if (!value || value.trim().length === 0) {
            return 'El nom d\'usuari és obligatori.';
        }
        if (value.length < 4 || value.length > 30) {
            return 'El nom d\'usuari ha de tener entre 4 i 30 caràcters.';
        }
        if (!/^[a-zA-Z0-9_]+$/.test(value)) {
            return 'El nom d\'usuari només pot contenir lletres, números i guion baix.';
        }
        return '';
    }

    function validateDNI(value) {
        if (!value || value.trim().length === 0) {
            return 'El DNI és obligatori.';
        }
        if (!/^[0-9]{8}[A-Z]$/.test(value)) {
            return 'El DNI ha de tener format: 8 dígits i 1 lletra majúscula (ex: 12345678A).';
        }
        return '';
    }

    function validateZip(value) {
        if (!value || value.trim().length === 0) {
            return 'El codi postal és obligatori.';
        }
        if (!/^[0-9]{5}$/.test(value)) {
            return 'El codi postal ha de contenir exactament 5 dígits.';
        }
        return '';
    }

    function validateCity(value) {
        if (!value || value.trim().length === 0) {
            return 'La ciutat és obligatòria.';
        }
        return '';
    }

    function validateCountry(value) {
        if (!value || value.trim().length === 0) {
            return 'El país és obligatori.';
        }
        return '';
    }

    function validatePhone(value) {
        // Teléfono es opcional
        if (!value || value.trim().length === 0) {
            return '';
        }
        if (!/^[0-9]{7,15}$/.test(value)) {
            return 'El telèfon ha de contenir entre 7 i 15 dígits.';
        }
        return '';
    }

    function validatePassword(value) {
        if (!value || value.trim().length === 0) {
            return 'La contrasenya és obligatòria.';
        }
        if (value.length < 8) {
            return 'La contrasenya ha de tener almenys 8 caràcters.';
        }
        if (!/[A-Z]/.test(value)) {
            return 'La contrasenya ha de contenir almenys una lletra majúscula.';
        }
        if (!/[0-9]/.test(value)) {
            return 'La contrasenya ha de contenir almenys un número.';
        }
        if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(value)) {
            return 'La contrasenya ha de contenir almenys un caràcter especial.';
        }
        return '';
    }

    function validatePasswordCheck(value) {
        const password = document.getElementById('password').value;
        if (!value || value.trim().length === 0) {
            return 'La confirmació de contrasenya és obligatòria.';
        }
        if (value !== password) {
            return 'Les contrasenyes no coincideixen.';
        }
        return '';
    }

    function showClientMessage(message, type) {
        clientMessage.textContent = message;
        clientMessage.className = 'message ' + type;
        setTimeout(() => {
            clientMessage.classList.add('hidden');
        }, 5000);
    }
});
