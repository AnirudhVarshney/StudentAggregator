package com.devionlabs.ray.studentaggregator;

/**
 * Created by ABHINAV on 27-04-2016.
 */
public class Fees {
    private int StudentId;
    private String Paidon;
    private int Amount;
    private String Description;
    private String Type;
    private int ComputerFee;
    private int TutionFee;
    private int LibraryFee;


    private int TotalSum;

    public Fees() {
    }


    public Fees(int StudentId, String Paidon, int Amount, String Description, String Type, int tutionFee, int computerFee, int libraryFee) {
        this.StudentId = StudentId;
        this.Paidon = Paidon;
        this.Amount = Amount;
        this.Description = Description;
        this.Type = Type;
        this.TutionFee = tutionFee;
        this.ComputerFee = computerFee;
        this.LibraryFee = libraryFee;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getDescription() {
        return Description;
    }

    public int getTotalSum() {
        return TotalSum;
    }

    public void setTotalSum(int totalSum) {
        TotalSum = totalSum;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPaidon() {
        return Paidon;
    }

    public void setPaidon(String paidon) {
        Paidon = paidon;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getTutionFee() {
        return TutionFee;
    }

    public void setTutionFee(int tutionFee) {
        TutionFee = tutionFee;
    }

    public int getComputerFee() {
        return ComputerFee;
    }

    public void setComputerFee(int computerFee) {
        ComputerFee = computerFee;
    }

    public int getLibraryFee() {
        return LibraryFee;
    }

    public void setLibraryFee(int libraryFee) {
        LibraryFee = libraryFee;
    }

    /*
    StudentId
    Paidon
    Amount
    Description
    Type
    ComputerFee
    TutionFee
    LibraryFee
    */

    @Override
    public String toString() {
        return "StudentId = " + StudentId + " : Paidon = " + Paidon + " : Amount = " + Amount +
                " : Description = " + Description + " : Type = " + Type + " : ComputerFee = " +
                ComputerFee + " : TutionFee = " + TutionFee + " : LibraryFee = " + LibraryFee;
    }
}

