package com.esthetic.reservations.api.util;

import java.util.regex.Pattern;

import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.UserEntity;

public class Util {

    public Util() {
    }

    public boolean isValidEmail(String email) {
        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(email).matches();
    }
    
    public String typeEmail(int type, UserEntity usuario, Branch sucursal, Appointment cita) {
    	switch (type) {
			case 1:
				return "Correo enviado por Esthetic Reservation con el motivo de mostrar su cita reservada\n\n"
			            + usuario.getName() + " " + usuario.getLastName() + "\n"
			            + "Gracias por agendar su cita en la sucursal " + sucursal.getBranchName() + "\n\n"
			            + "Fecha de la cita: " + cita.getAppointment_Date() + "\n"
			            + "Hora de la cita: " + cita.getAppointmnet_time() + "\n\n\n"
			            + "Favor de mostrar el código QR en la sucursal";

			case 2:
				return "Correo enviado por Esthetic Reservation con el motivo de mostrar su cita actualziada\n\n"
			            + usuario.getName() + " " + usuario.getLastName() + "\n"
			            + "Gracias por actualizar su cita en la sucursal " + sucursal.getBranchName() + "\n\n"
			            + "Fecha de la cita: " + cita.getAppointment_Date() + "\n"
			            + "Hora de la cita: " + cita.getAppointmnet_time() + "\n\n\n"
			            + "Favor de mostrar el código QR en la sucursal";
			case 3:
				return "Correo enviado por Esthetic Reservation con el motivo de cambiar tu contraseña\n\n"
		                + usuario.getName() + " " + usuario.getLastName() + "\n"
		                + "Hacer click en el siguiente enlace para cambiar tu contraseña \n\n"
		                + AppConstants.BASE_URL + "app/reestablecer/password/update/" + usuario.getId()
		                + "\n\n"
		                + "De no haber requerido este correo, favor de ignorarlo";
			case 4:
				return "Correo enviado por Esthetic Reservation con el motivo de cancelación de cita reservada\n\n"
						+ "La sucursal " + sucursal.getBranchName() + " le ofrece una sincera disculpa al incoveniente sucedido "
						+ "pero por motivos personales y de fuerza mayor, se ve obligado a cancelar la cita programada para el " + cita.getAppointment_Date()
						+ " a las " + cita.getAppointmnet_time() + "\n\n"
						+ "Sé lo importante que es respetar el tiempo de los demás, por lo que lamento cualquier inconveniente que esta cancelación pueda causarle"
						+ "Aprecio su comprensión en este asunto y espero poder programar otra cita en el futuro\n\n\n";
			default:
				return "";
		}
    }

}
