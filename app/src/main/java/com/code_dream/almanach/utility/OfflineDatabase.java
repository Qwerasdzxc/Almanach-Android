package com.code_dream.almanach.utility;

import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.models.SubjectListItem;
import com.code_dream.almanach.models.TimetableItem;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

/**
 * Created by Peter on 7/28/2017.
 */

public class OfflineDatabase {

    public static OfflineDatabase instance;

    public Nitrite db;

    public ObjectRepository<TimetableItem> timetableItemRepo;
    public ObjectRepository<CalendarEvent> calendarEventsRepo;
    public ObjectRepository<SubjectListItem> subjectsRepo;
    public ObjectRepository<Note> notesRepo;

    public OfflineDatabase(String rootFilePath, String dbName) {
        instance = this;

        db = Nitrite.builder()
                .compressed()
                .filePath(rootFilePath + "/" + dbName + ".db")
                .openOrCreate();

        timetableItemRepo = db.getRepository(TimetableItem.class);
        calendarEventsRepo = db.getRepository(CalendarEvent.class);
        subjectsRepo = db.getRepository(SubjectListItem.class);
        notesRepo = db.getRepository(Note.class);
    }
}
