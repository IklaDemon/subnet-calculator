JAVAC = javac
JAVA = java
SRC = .
SOURCES = $(wildcard $(SRC)/*.java)
CLASSES = $(SOURCES:.java=.class)
MAIN_CLASS = Test

all: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

run: all
	$(JAVA) $(MAIN_CLASS)

clean:
	@del .\*.class
	@echo deleted all class files