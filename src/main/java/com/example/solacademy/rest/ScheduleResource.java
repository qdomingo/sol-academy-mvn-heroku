package com.example.solacademy.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.solacademy.model.Schedule;
import com.example.solacademy.service.ScheduleService;

@RestController
@CrossOrigin(origins = {"http://solacademy.qdomingo.com", "http://localhost:4200", "http://localhost:4300",
		"http://solacademy-aws.qdomingo.com:4200"})
@RequestMapping("/api/schedules")
public class ScheduleResource {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getAll")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
       
    @GetMapping("/getScheduleById/{id}")
    public List<Schedule> getScheduleById(@PathVariable String id) {
        return scheduleService.getScheduleById(Long.parseLong(id, 10));
    }
    
    @GetMapping("/getScheduleByStudentId/{id}")
    public List<Schedule> getScheduleBStudentyId(@PathVariable String id) {
        return scheduleService.getScheduleByStudentId(Long.parseLong(id, 10));
    }
    
    @PostMapping("/createSchedule")
    public int createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }
    
    @PostMapping("/updateSchedule")
    public int updateSchedule(@RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(schedule);
    }
    
    @PostMapping("/updateScheduleSerie")
    public int updateScheduleSerie(@RequestBody Schedule schedule) {
        return scheduleService.updateScheduleSerie(schedule);
    }
    
    @PostMapping("/deleteSchedule")
    public int deleteSchedule(@RequestBody Long id) {
        return scheduleService.deleteSchedule(id);
    }
    
    @PostMapping("/deleteScheduleSerie")
    public int deleteScheduleSerie(@RequestBody Schedule schedule) {
        return scheduleService.deleteScheduleSerie(schedule);
    }
	
}
