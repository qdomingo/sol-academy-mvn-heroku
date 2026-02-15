package com.example.solacademy.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solacademy.mapper.ScheduleMapper;
import com.example.solacademy.mapper.StudentMapper;
import com.example.solacademy.model.Payment;
import com.example.solacademy.model.Schedule;

@Service
@Transactional
public class ScheduleService {
	
	@Autowired
    private ScheduleMapper scheduleMapper;
	
	@Autowired
    private StudentMapper studentMapper;

    public List<Schedule> getAllSchedules() {
        return scheduleMapper.getAllSchedules();
    }
    
    public List<Schedule> getScheduleById(Long id) {
        return scheduleMapper.getScheduleById(id);
    }
    
    public List<Schedule> getScheduleByStudentId(Long id) {
        return scheduleMapper.getScheduleByStudentId(id);
    }
    
    public int createSchedule(Schedule schedule) {
    	
    	int resultado = 0;
    	int resultadoPayment = 0;
    	int rate = 10;
    	Long maxId = scheduleMapper.getMaxScheduleID();
    	if(schedule.getStudent_id() != null && schedule.getStudent_id() != 0) {
    		rate = studentMapper.getRateStudentById(schedule.getStudent_id());
    	}
    	
    	if(schedule.getWeekly() == 1) {

    		// Creamos serie_id
    		String serie_id = schedule.getStudent_id() + "_" + schedule.getInicio().substring(10, 16);
    		schedule.setSerie_id(serie_id);
    		
    		// primera persistencia
    		schedule.setId(maxId);
    		resultado = scheduleMapper.createSchedule(schedule);
    		Payment payment = setPayment(schedule, true, rate);
    		resultadoPayment = studentMapper.createPayment(payment);
    		
    		// Convertir la cadena a ZonedDateTime
            ZonedDateTime dateTimeInicio = ZonedDateTime.parse(schedule.getInicio());
            ZonedDateTime dateTimeFin = ZonedDateTime.parse(schedule.getFin());
    		
    		// añadir logica para repetir semanalmente durante 1 año
        	for(int i=1;i<53;i++) {
        		
                // aumentamos maxId
                maxId++;
        		                
                // Sumar 7 días
                ZonedDateTime newDateTimeInicio = dateTimeInicio.plus(i*7, ChronoUnit.DAYS);
                ZonedDateTime newDateTimeFin = dateTimeFin.plus(i*7, ChronoUnit.DAYS);
                
                // Ajustar la hora para el horario de verano si es necesario
                // Primero comprobamos si ahora estamos en horario de verano o no
                //		Si NO estamos en horario de verano, para cuando lo estemos necesitamos restar una hora
                //		Si SI estamos en horario de verano, para cuando no lo estemos necesitaremos añadir una hora
                if(!ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(dateTimeInicio.toInstant())) {
                    if (ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(newDateTimeInicio.toInstant())) {
                    	newDateTimeInicio = newDateTimeInicio.minusHours(1);
                    	newDateTimeFin = newDateTimeFin.minusHours(1);
                    }
                } else {
                    if (!ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(newDateTimeInicio.toInstant())) {
                    	newDateTimeInicio = newDateTimeInicio.plusHours(1);
                    	newDateTimeFin = newDateTimeFin.plusHours(1);
                    }
                }

                // Convertir la nueva fecha y hora a cadena con formato ISO
                String newIsoDateTimeInicioString = newDateTimeInicio.format(DateTimeFormatter.ISO_INSTANT);
                String newIsoDateTimeFinString = newDateTimeFin.format(DateTimeFormatter.ISO_INSTANT);
                
                // seteamos los campos
                schedule.setInicio(newIsoDateTimeInicioString);
                schedule.setFin(newIsoDateTimeFinString);
                schedule.setId(maxId);
                
                //persitimos
                resultado = scheduleMapper.createSchedule(schedule);
                
                // añadir logica para crear los pagos
        		payment = setPayment(schedule, true, rate);
        		resultadoPayment = studentMapper.createPayment(payment);

        	}
    		schedule.setId(maxId);
    	} else {
    		schedule.setId(maxId);
    		resultado = scheduleMapper.createSchedule(schedule);
    		
    		// añadir logica para crear los pagos
    		Payment payment = setPayment(schedule, true, rate);
    		resultadoPayment = studentMapper.createPayment(payment);
    	}
    	
        return resultado + resultadoPayment;
    }
    
    public int updateSchedule(Schedule schedule) {
    	
    	// schedule update
    	int updateSchedule = scheduleMapper.updateSchedule(schedule);
    	int rate = 0; // esta tasa no la updateamos, no afecta

    	// payment update
    	int updatePayment = 1;
        Payment payment = setPayment(schedule, false, rate);
        updatePayment = studentMapper.updatePayment(payment);

        return updateSchedule + updatePayment;
    }
    
    public int updateScheduleSerie(Schedule schedule) {
    	
    	int updateSchedule = 1; // valores de devolucion del update schedule
    	int updatePayment = 1; // valores de devolucion del update payment
    	int rate = 0; // esta tasa no la updateamos, no afecta
    	
    	// Creamos la nueva serie_id
		String serie_id = schedule.getStudent_id() + "_" + schedule.getInicio().substring(10, 16);
		schedule.setSerie_id(serie_id);
		
		// primera persistencia
		updateSchedule = scheduleMapper.updateScheduleSerie(schedule);
    	
		Payment payment = setPayment(schedule, true, rate);
		updatePayment = studentMapper.updatePaymentSerie(payment);
		
		// Convertir la cadena a ZonedDateTime
        ZonedDateTime dateTimeInicio = ZonedDateTime.parse(schedule.getInicio());
        ZonedDateTime dateTimeFin = ZonedDateTime.parse(schedule.getFin());
        // obtener idInicial para modificarlo sacuencialmente
        Long idInicial = schedule.getId();
		
		// añadir logica para repetir semanalmente durante 1 año
    	for(int i=1;i<53;i++) {
    		// aumentar el id
    		Long id = idInicial+i;
    		
            // Sumar 7 días
            ZonedDateTime newDateTimeInicio = dateTimeInicio.plus(i*7, ChronoUnit.DAYS);
            ZonedDateTime newDateTimeFin = dateTimeFin.plus(i*7, ChronoUnit.DAYS);
            
            // Ajustar la hora para el horario de verano si es necesario
            // Primero comprobamos si ahora estamos en horario de verano o no
            //		Si NO estamos en horario de verano, para cuando lo estemos necesitamos restar una hora
            //		Si SI estamos en horario de verano, para cuando no lo estemos necesitaremos añadir una hora
            if(!ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(dateTimeInicio.toInstant())) {
                if (ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(newDateTimeInicio.toInstant())) {
                	newDateTimeInicio = newDateTimeInicio.minusHours(1);
                	newDateTimeFin = newDateTimeFin.minusHours(1);
                }
            } else {
                if (!ZoneId.of("Europe/Madrid").getRules().isDaylightSavings(newDateTimeInicio.toInstant())) {
                	newDateTimeInicio = newDateTimeInicio.plusHours(1);
                	newDateTimeFin = newDateTimeFin.plusHours(1);
                }
            }

            // Convertir la nueva fecha y hora a cadena con formato ISO
            String newIsoDateTimeInicioString = newDateTimeInicio.format(DateTimeFormatter.ISO_INSTANT);
            String newIsoDateTimeFinString = newDateTimeFin.format(DateTimeFormatter.ISO_INSTANT);
            
            // seteamos los campos
            schedule.setInicio(newIsoDateTimeInicioString);
            schedule.setFin(newIsoDateTimeFinString);
            schedule.setId(id);
                		
        	// schedule update
        	updateSchedule = scheduleMapper.updateScheduleSerie(schedule);
        	
        	// payment update
            payment = setPayment(schedule, false, rate);
            updatePayment = studentMapper.updatePaymentSerie(payment);
    		
    	}

        return updateSchedule + updatePayment;
    }
    
    public int deleteSchedule(Long id) {
    	int deleteSchedule = scheduleMapper.deleteSchedule(id);
    	int deletePayment = studentMapper.deletePayment(id);
        return deleteSchedule + deletePayment;
    }
    
    public int deleteScheduleSerie(Schedule schedule) {
    	Long id = schedule.getStudent_id();
    	String serie_id = schedule.getSerie_id();
    	String inicio = schedule.getInicio();
    	int deleteScheduleSerie = scheduleMapper.deleteScheduleSerie(id, serie_id, inicio);
    	int deletePaymentsSerie = studentMapper.deletePaymentsSerie(id, serie_id, inicio);
        return deleteScheduleSerie + deletePaymentsSerie;
    }
    
    public Payment setPayment (Schedule schedule, boolean creacion, int rate) {
    	
    	Payment payment = new Payment();
    	payment.setSchedule_id(schedule.getId());
    	payment.setInicio(schedule.getInicio());
    	payment.setFin(schedule.getFin());
    	payment.setTitle(schedule.getTitle());
    	payment.setStudent_id(schedule.getStudent_id());
    	payment.setSerie_id(schedule.getSerie_id());
    	
    	//calculo de la duracion
        // Formato de las fechas y Fechas en formato de cadena
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dateStr1 = schedule.getInicio().substring(0, 16);
        String dateStr2 = schedule.getFin().substring(0, 16);

        // Convertir las cadenas a objetos LocalDateTime
        LocalDateTime dateTime1 = LocalDateTime.parse(dateStr1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateStr2, formatter);

        // Calcular la diferencia entre las dos fechas y Obtener la diferencia en minutos
        Duration duration = Duration.between(dateTime1, dateTime2);
        long differenceInMinutes = duration.toMinutes();
        
        payment.setDuration((int)differenceInMinutes);
        
        // seteamos campos necesarios en la creacion, pero no en el update
        if (creacion) {
        	payment.setPaid(0);
        	payment.setRate(rate);
        }
    	
    	return payment;
    }

}
