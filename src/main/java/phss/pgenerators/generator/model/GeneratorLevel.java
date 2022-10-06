package phss.pgenerators.generator.model;

public interface GeneratorLevel {

    Integer getLevel();
    Integer getInterval();
    Integer getAmount();
    Integer getPrice();

    void upgrade();

}
