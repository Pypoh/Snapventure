package com.raion.snapventure.Data;

public class DataUserStage {
    public static final String TABLE_NAME = "user_stage";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DIFF = "DIFFICULTY";
    public static final String COLUMN_STAGE = "STAGE";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_POINT_STATUS = "POINT_STATUS";
    public static final String COLUMN_RIDDLE_EN = "RIDDLE_EN";
    public static final String COLUMN_RIDDLE_ID = "RIDDLE_ID";
    public static final String COLUMN_ANSWER = "ANSWER";

    private int id;
    private String difficulty, stage, riddleEn, riddleId, answer;
    private boolean status, point_status;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DIFF + " TEXT, " + COLUMN_STAGE + " TEXT, "
                + COLUMN_STATUS + " INTEGER, " + COLUMN_POINT_STATUS + " INTEGER,"
                + COLUMN_RIDDLE_EN + " TEXT, " + COLUMN_RIDDLE_ID + " TEXT, "
                + COLUMN_ANSWER + " TEXT)";

    public DataUserStage() {
    }

    public DataUserStage(int id, String difficulty, String stage, String riddleEn, String riddleId, String answer, int status, int point_status) {
        this.id = id;
        this.difficulty = difficulty;
        this.stage = stage;
        this.riddleEn = riddleEn;
        this.riddleId = riddleId;
        this.answer = answer;
        if (status > 0) {
            this.status = true;
        }
        this.point_status = point_status > 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status > 0) {
            this.status = true;
        } else {
            this.status = false;
        }
    }

    public boolean isPoint_status() {
        return point_status;
    }

    public void setPoint_status(int point_status) {
        if (point_status < 0) {
            this.point_status = true;
        } else {
            this.point_status = false;
        }
    }

    public String getRiddleEn() {
        return riddleEn;
    }

    public void setRiddleEn(String riddleEn) {
        this.riddleEn = riddleEn;
    }

    public String getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(String riddleId) {
        this.riddleId = riddleId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
