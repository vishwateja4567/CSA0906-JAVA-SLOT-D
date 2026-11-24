Task 2 – From Requirement to Event-Driven Design
Problem: Hospital Outpatient Appointment Desk

This task mainly requires an event table, component hierarchy, pseudocode and event trace. A Java implementation is not explicitly required.

2.1 Event table
Event source

Event object

Listener

Action

Patient ID field

ActionEvent

ActionListener

Read patient ID

Department list

ItemEvent

ItemListener

Refresh doctor list

Doctor list

ItemEvent

ItemListener

Select doctor

Time slot

ItemEvent

ItemListener

Select time

Book button

ActionEvent

ActionListener

Validate and book

Window close

WindowEvent

WindowListener

Confirm before closing

2.2 Component hierarchy
JFrame (Appointment Desk)
 |
 +-- JPanel (Patient Details)
 |    +-- JLabel (Patient ID)
 |    +-- JTextField
 |
 +-- JPanel (Appointment Details)
 |    +-- JComboBox (Department)
 |    +-- JComboBox (Doctor)
 |    +-- JComboBox (Time Slot)
 |
 +-- JPanel (Actions)
 |    +-- JButton (Book)
 |    +-- JButton (Cancel)
 |
 +-- JLabel (Status Message)
2.3 Pseudocode
A. Refresh doctor list

BEGIN
    Read selected department
    Clear doctor list
    Get doctors for selected department
    Add doctors to doctor list
    Update doctor dropdown
END
B. Book appointment

BEGIN
    Read patient ID
    Read department
    Read doctor
    Read time slot

    IF any field is empty THEN
        Display error message
    ELSE
        Save appointment
        Display booking confirmation
    END IF
END
2.4 Expected output
HOSPITAL APPOINTMENT DESK

Patient ID: 192425326
Department: Cardiology
Doctor: Dr. Kumar
Time Slot: 10:00 AM

Appointment Booked Successfully!
Event sequence: Open window → Initialize components → Select department → Refresh doctors → Select doctor → Select time → Book appointment → Confirm closing → Close window.

Applet comparison: An applet uses init(), start(), paint(), stop() and destroy(), whereas this GUI uses event listeners and window events.
