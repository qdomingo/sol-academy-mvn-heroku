package com.example.solacademy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.solacademy.mapper.ScheduleMapper;
import com.example.solacademy.mapper.StudentMapper;
import com.example.solacademy.model.Payment;
import com.example.solacademy.model.Student;
import com.example.solacademy.model.Task;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    private S3Service s3Service;

    public List<Student> getAllStudents() {
    	List<Student> studentlist = studentMapper.getAllStudents();
    	for(Student student: studentlist) {
    		List<Task> studentTasks = studentMapper.getTasksByStudentId(student.getId());
    		student.setTasks(studentTasks);
    	}
        return studentlist;
    }
    
    public List<Student> getStudentById(Long id) {
    	List<Task> studentTasks = studentMapper.getTasksByStudentId(id);
    	List<Student> student = studentMapper.getStudentById(id);
    	if(!student.isEmpty()) {
    		student.get(0).setTasks(studentTasks);
    	}
        return student;
    }
    
    public int createStudent(Student student) {
    	Long studentId = studentMapper.getMaxStudentID();
    	String studentIdString = studentId.toString();
    	student.setId(studentId);
    	int createStudent = studentMapper.createStudent(student);
    	// crear tb carpeta en S3 para el estudiante
    	s3Service.createFolder("", studentIdString);
        return createStudent;
    }
    
    public int updateStudent(Student student) {
    	int updateStudent = studentMapper.updateStudent(student);
    	// actualizamos rate de payments, por si cambia
    	int updatePayments = studentMapper.updateStudentPaymentRate(student.getId(), student.getRate());
        return updateStudent + updatePayments;
    }
    
    public int deleteStudent(Long id) {
    	int deleteSchedulesStudent = scheduleMapper.deleteStudentSchedule(id);
    	int deleteTasksByStudent = studentMapper.deleteTasksByStudent(id);
    	int deletePaymentsByStudent = studentMapper.deletePaymentsByStudent(id);
    	int deleteStudent = studentMapper.deleteStudent(id);
        return deleteSchedulesStudent + deleteTasksByStudent +deleteStudent;
    }
    
    public List<Task> getTasksByStudentId(Long id) {
        return studentMapper.getTasksByStudentId(id);
    }
    
    public int createTask(Task task) {
    	task.setId(studentMapper.getMaxTaskID());
    	int createTask = studentMapper.createTask(task);
    	int updateSchedule = 0;
    	// si la task tiene schedule asociado actualizamos el schedule con el numero de la task
    	if(task.getSchedule_id() != null ) {
    		scheduleMapper.updateScheduleTask(task.getSchedule_id(), task.getId());
    	}
        return createTask + updateSchedule;
    }
    
    public int updateTask(Task task) {
    	// al updatear una task, ponemos a null el valor task_id de cualquier schedule 
    	// que tenga asociada esta task
    	int resetTask = scheduleMapper.resetScheduleTask(task.getId());
    	int updateTask = studentMapper.updateTask(task);
    	int updateSchedule = 0;
    	// si la task tiene schedule asociado actualizamos el schedule con el numero de la task
    	if(task.getSchedule_id() != null ) {
    		updateSchedule = scheduleMapper.updateScheduleTask(task.getSchedule_id(), task.getId());
    	}
        return updateTask + updateSchedule;
    }
    
    public int deleteTask(Task task) {
    	int updateSchedule = 0;
    	// actualizamos tambien schedule si tiene schedule asociado
    	if(task.getSchedule_id() != null ) {
    		updateSchedule = scheduleMapper.updateScheduleTask(task.getSchedule_id(), null);
    	}
    	// borramos la tarea
    	int deleteTask = studentMapper.deleteTask(task.getId());
        return deleteTask + updateSchedule;
    }
    
    public List<Payment> getPaymentsByStudentId(Long id) {
        return studentMapper.getPaymentsByStudentId(id);
    }
    
    public int createPayment(Payment payment) {
        return studentMapper.createPayment(payment);
    }
    
    public int updatePayment(Payment payment) {
        return studentMapper.updatePayment(payment);
    }
    
    public int deletePayment(Long id) {
        return studentMapper.deletePayment(id);
    }

}
