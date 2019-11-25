- [CS 1632 - Software Quality Assurance](#cs-1632---software-quality-assurance)
  * [Overview](#Overview)
  * [Background](#background)
  * [Exploratory Testing](#exploratory-testing)
    + [Luck Mode](#luck-mode)
    + [Skill Mode](#skill-mode)
  * [What to do](#what-to-do)
    + [Automated Unit Test Writing](#automated-unit-test-writing)
    + [Model Checking Property Writing](#model-checking-property-writing)
    + [Coding](#coding)
    + [Static Testing](#static-testing)
    + [Manual System Testing](#manual-system-testing)
  * [Format](#format)
  * [Grading](#grading)
  * [Submission](#submission)
  
# CS 1632 - Software Quality Assurance
Fall Semester 2019

DUE: December 12, 2019 11:59 PM (No extensions due to grading deadline)

## Overview

For this final deliverable, you and your partner will develop a full-fledged
GUI program (with the help of some skeleton code), along with various tests.
You will be using many of the concepts we have learned during the semester:
test-driven development (TDD), exploratory testing, automated unit testing,
code coverage, manual unit testing, static testing, and model checking.  I
expect the program to be rigorously tested.  Any defect will be cause for point
deduction.

* IMPORTANT: You need Java 8 (1.8.0.231, preferably) to run JPF.  Make sure you
  have the correct Java version by doing "java -version" and "javac -version"
before going into the JPF section.

## Background

The bean counter is a device for statistics experiments devised by English
scientist Sir Francis Galton. It consists of an upright board with evenly
spaced pegs in a triangular form.  Beans are dropped from an opening at the top
of the board. Every time a bean hits a peg, it has a 50% chance of falling to
the left or to the right.  In this way, each bean takes a random path and
eventually falls into one of the slots at the bottom of the board.  After all
the beans fall through, the number of beans in each slot is counted.

See the following link for a more detailed description of the machine:
https://en.wikipedia.org/wiki/Bean_machine.

The bean counter had two contributions to statistics by demonstrating the following:
1. When the sample size is large enough, a [binomial distribution](https://en.wikipedia.org/wiki/Binomial_distribution) approaches a [bell curve](https://en.wikipedia.org/wiki/Normal_distribution).
2. It also demonstrated a phenomenon named [regression to the mean](https://en.wikipedia.org/wiki/Regression_toward_the_mean).

Regression to the mean had been (and still is) a source of numerous scientific
misconceptions.  People make conjectures all the time about all types of things
and provide reasons for it.

1. Why is my favorite sports team performing in a mediocre way when it won the championships last year?  Because my favorite player was traded.
2. Why did the crime rate in my city fall down to the national average?  Due to better policing.
3. Why did a student who did exceptionally well on the midterms perform just about average on the finals?  Because the student slacked off.

People always look for reasons for changes in data.  But often the reason
cannot be explained, because there was no reason for the change to begin with.
The change in data can just be due to a statistical anomaly named regression to
the mean.  For example, an answer to question 3 can simply be that the student
was exceptionally lucky during the midterms (she guessed all multiple choices
and she got them all correct).  Her luck wore off and she just got what she
deserved in the finals.  This is called regression to the mean.  Now if the
exceptional score was due to skill, then the regression would not happen unless
there was a regression in skill.  The problem is, it is very hard to tell
whether something was due to luck or skill just by looking at the results,
hence the numerous misconceptions.

## Exploratory Testing

The program simulates a bean machine with 10 slots at the bottom (0-9).

Let's first compile the program by running compile.bat:
```
$ compile.bat
```
Mac/Linux users, please run compile.sh.  Run the .sh extension for all .bat files that follow.

The program is executed with two commandline arguments:
```
$ java -cp bin BeanCounterGUI
Usage: java BeanCounterGUI <number of beans> <luck | skill>
Example: java BeanCounterGUI 500 luck 
```

The second argument "luck" or "skill" decides whether individual beans will use
luck or skill in navigating the bean machine.  

Let's do some exploratory testing.  You could use the class files that you have
just compiled, but the app doesn't do much at this point because the internal
logic has not yet been implemented (by you).  Instead, let's use a reference
implementation (that I wrote) named BeanCounter.jar.

### Luck Mode

In luck mode, the bean counter operates conventionally as originally built by
Galton: a bean has an equal chance of going left or right on a peg.  So where
the bean lands at the bottom is purely due to luck.  Hence, you would expect
the beans to be heavily susceptible to regression to the mean.  Try the following:

1. Run BeanCounter.jar in luck mode:
```
$ java -jar BeanCounter.jar 500 luck
```
2. Press the "Fast" button to fast-forward to the end.
3. Note the average (should be close to 4.5 = 0 + 9 / 2).
4. Press the "Upper Half" button to just take the upper half of the sample.
5. Note the average (now it should be much higher since it's the upper half).
6. Press the "Repeat" button to scoop all the beans and bring them to the top.
7. Press the "Fast" button to restart the machine.
8. Note the average is again close to 4.5.

You have just observed regression to the mean.  You took the upper half of the
class, but when they were put through the exam again, they scored just about
average.  Did they slack off in the second exam?  No, they were just no better
than the other students to begin with.

### Skill Mode

In skill mode, the beans choose direction based on pure skill.  Each bean is
assigned a skill level from 0-9 on creation according to a bell curve with
average 4.5 and standard deviation 1.5.  A skill level of 9 means the bean
always makes the "right" choices (pun intended).  That means the bean will
always go right when a peg is encountered, resulting it falling into the
rightmost 9th slot. A skill level of 0 means that the bean will always go left,
resulting it falling into the leftmost 0th slot. For the in-between skill
levels, the bean will first go right then left. For example, for a skill level
of 7, the bean will go right 7 times then go left twice.  So where the bean
lands at the bottom would be determined entirely by the skill level of the
bean.  Try the following:

1. Run BeanCounter.jar in skill mode:
```
$ java -jar BeanCounter.jar 500 skill
```
2. Press the "Fast" button to fast-forward to the end.
3. Note the average (should be close to 4.5 = 0 + 9 / 2).
4. Press the "Upper Half" button to just take the upper half of the sample.
5. Note the average (now it should be much higher since it's the upper half).
6. Press the "Repeat" button to scoop all the beans and bring them to the top.
7. Press the "Fast" button to restart the machine.
8. Note the average is exactly the same as the average noted in Step 5.

You will observe that the average does not change at all when you repeat the
experiment with the upper half of the samples.  There is no regression to the
mean because the results are pre-determined by skill level.  In this case, the
student performed well on the first exam because they were actually skilled!

Also, you will notice the slots filling sequentially one by one in the repeat
run.  This is a side-effect of the slots at the bottom being collected one by
one when the repeat button is pressed.  All the beans in one slot have the same
skill level so the beans naturally get sorted out as a result of the
collection.

Try out other features of the program by pressing different buttons.

## What to do

I expect you to employ test-driven development (TDD) for this project and fully
embrace it.  I can guarantee you that it will shorten development time.  You
are going to write the tests anyway.  Why not write them at the beginning when
they will be much more useful?  I will lay down the steps, roughly in the order
you should perform them.

### Automated Unit Test Writing

Write JUnit tests for each of the methods you are asked to implement marked by
the "// TODO" comment in the files: BeanCounterLogic.java and Bean.java.  You
will probably need to add more methods of your own other than the ones
specified.  For all these methods, write at least *one unit test each*.
Coverage for both classes should be above *80%* each.  Two files have been
created for you for this purpose: BeanCounterLogicTest.java and BeanTest.java.
You can run the JUnit test using TestRunner as usual, using the following
script:

```
$ runJUnit.bat
```

Make use of mocks and test doubles as necessary.  You should not test a
dependent class along with your target class.  Neither should you introduce
randomness during test.  You will notice that I have designed the code using
dependency injection so that this is relatively easy to do.

Initially, the unit tests should fail since you haven't written the code.

Note: in order to achieve high coverage, you may have to unit test the *main()*
method for BeanCounterLogic this time.  In general, main() methods are not
amenable to unit testing because they are, by design, not amenable to
dependency injection.  But there are cases where the main() method is too large
to ignore in terms of test coverage.  Ideally, the main() method is small
enough to ignore, but testers do not get to choose the code they test (sigh).

In principle, writing a unit test for a main() method is exactly the same as
for any other method.  You pass the commandline arguments you want to test to
the "String[] args" argument and that's it.  You will not be able to perform
dependency injection or mocking so you will be forced to test the Bean class
along with the target BeanCounterLogic class, but c'est la vie.  Review Chapter
14.6 "Testing System Output" in the textbook in order to test system output
against the requirements.

You can also test the JPF code inside the main() method by passing "test" as
the commandline argument.  But you will then have to have the JPF libraries in
the classpath and also ensure that you are running Java 8 (see above).  If you
are using Eclipse, that means adding those libraries and JRE System Library
(jdk 1.8) to your Project > Properties > Java Build Path > Libraries.  You also
need to add them to runJUnit.bat or runJUnit.sh if you are running from the
commandline.  They are there in the most recent version on github (as of
11/23).

### Model Checking Property Writing

Write invariant property assertions for the Java Pathfinder (JPF) model
checker.  You will notice that BeanCounterLogic.java has an alternate main()
method.  This main() method is used to provide a rudimentary text user
interface.  You can invoke it by doing:
```
java -cp BeanCounter.jar BeanCounterLogic 500 luck
```

In addition, it serves as a route to model check the
BeanCounterLogic class (when "test" is passed as a commandline argument).  I
have intentionally separated out the logic part of the program from the GUI
explicitly for the purposes of model checking.  Model checking a GUI is tricky
and so is a multi-threaded event-driven program like BeanCounterGUI.  Yes, JPF
can model check even multi-threaded programs (!) by exhaustively going through
all the interleavings.  But it is complicated and it takes a long time.  So we
will just check the internal logic, which is the important part anyway.  You
can run JPF by using the following script:

```
$ runJPF.bat BeanCounter.jpf
```

In order to have JPF run the BeanCounter, you will first have to let it explore
a range of bean count and slot count parameters.  As described in the "// TODO"
comment in the main() method, use the Verify API we learned during the exercise
to assign 0-3 bean count and 1-5 slot count values.  We will not test slot
count 0 because then it means there are no slots to receive beans and the
machine basically falls apart.  These are enough values to give us confidence
that our machine satisfies system properties.

Remember, JPF only checks for the system property that no exceptions are raised
if there are no user assertions.  As of now, there is only a single invariant
assertion in main() method: the one that checks that in-flight beans are at
legal positions at every step of the experiment.  Add two more assertions, as
is described in the "// TODO" comments in the main() method.  _Also add at least one assertion of your own that helps you verify some system property_.

With these additional assertions, the model checker should fail since you
haven't written the code yet.  Write your code towards satisfying the assertions.  After satisfying, frequently run the model checker while coding to verify that the system properties are not violated, 

If you notice JPF running slower while coding, this is due to state explosion.
You need to filter out state irrelevant to the assertions using the
@FilterField annotation we learned during the exercise.  In my code, I had 684
states in the model checker (as can be seen in the "new=684" number below):

```
====================================================== statistics
elapsed time:       00:00:00
states:             new=684,visited=695,backtracked=1379,end=448
...
```

Also note that it took less than a second to run the model checker.  In your
report, you will have to show a screenshot of the above and demonstrate it took
you less than 10 seconds in order to get full credit.  If not, test a smaller
range of numbers that pass in 10 seconds and you will get partial credit.

Use the following links as reference:  
https://github.com/javapathfinder/jpf-core/wiki  
http://javapathfinder.sourceforge.net/

### Coding

All the GUI coding has already been done for you, since some of you are not
familiar with Java AWT and event-driven programming.  You only need to
implement the logic of the machine.  All the parts that you have to fill in
have been commented with // TODO in the files: BeanCounterLogic.java and
Bean.java.  You will not need to modify any of the other files.  As you are
coding, regularly run the unit tests and the model checker, both to check that
the coded feature was properly implemented and that you have not regressed.
Your goal in coding should be to make those tests pass.

In order to get the bell curve in skill mode, you will have to use the
Random.nextGaussian() method.  A bell curve is synonymous with normal
distribution is synonymous with Gaussian distribution, hence the name.  Here is
the formula you should use:

```
skill = nextGaussian() * SKILL_STDEV + SKILL_AVERAGE
```

SKILL\_AVERAGE and SKILL\_DEV are the average and standard deviation of the
skill values and are pre-computed for you in the Bean.java skeleton code,
according to the bell curve that [approximates the binomial
distribution](https://en.wikipedia.org/wiki/Binomial_distribution#Normal_approximation)
of the beans in luck mode.

### Static Testing

Run the CheckStyle linter and the SpotBugs tool regularly while and after
coding.  When you are done, both tools should show a successful audit:

```
$ runCheckstyle.bat
Starting audit...
Audit done.
```

```
$ runSpotbugs.bat
The following classes needed for analysis were missing:
  org.junit.runner.JUnitCore
  org.junit.runner.Result
  org.junit.runner.notification.Failure
```

You can consider the above to be a successful audit.  The warning is just
showing that SpotBugs could not access the JUnit classes (which is a good thing
because you will see dozens of flagged "bugs" in the JUnit code base found by
SpotBugs that have nothing to do with you).

### Manual System Testing

Even after doing your unit tests and model checking, you still need to verify
that the program "looks" right end-to-end in the GUI.  This is hard to do using
automated testing so you will write manual test cases for this.  Refer to the
[requirements.md](requirements.md) file for the features that need testing.  Each feature should
have at least *one test case each*.  Also you will show that you have covered
all your bases by writing a *traceability matrix*.

## Format

Every assignment should have a title page with:
* Your name and your partner name
* The URL of your code and tests on GitHub
* The title "CS 1632 - DELIVERABLE 5: End-to-end Testing BeanCounter"

Write a short summary (< 1 page) of your experience...
1. ...with Test Driven Development (TDD).  In what ways did it help you?  In what ways did it hinder you?
2. ...with multiple layers of testing.  You used unit testing, model checking, static testing, and manual systems testing, at various layers.  Which did you find the most valuable?  Which did you find the least valuable?

ON A SEPARATE PAGE, label the section "Unit Testing" and include a screenshot of code coverage that shows at least 80% coverage for each of BeanCounterLogic and Bean classes, when running TestRunner.

ON A SEPARATE PAGE, label the section "Model Checking" and include a screenshot or copy-and-paste of the results of running runJPF.bat.  It should show no errors detected and the elapsed time should be less than 10 seconds.  Also, write a description of one additional assertion you added to check a system property, and why you chose to test that property.

ON A SEPARATE PAGE, label the section "Manual System Testing" and write all test cases.  Each requirement should be tested by at least one test case.  Each test case should include the requisite IDENTIFIER, TEST CASE, PRECONDITIONS, EXECUTION STEPS, and POSTCONDITIONS.  Your test cases should be concise but precise.  Screenshots are allowed.  

At the very end, add a traceability matrix.

## Grading

Please review [grading_rubric.txt](grading_rubric.txt) for details.

Note that there is Extra Credit under the discretion of the instructor and TA.

Also note there are NO LATE SUBMISSIONS due to the grading deadline.  Any submission issues (such as inaccessible repository) may result in a 0.

## Submission

Each pairwise group will submit the deliverable once to courseweb, by one member of the group. Under the "Course Documents" menu on the lefthand side, you will see an assignment named "Deliverable 5". Please upload a PDF format of your report.  Don't forget your github url.  Also don't forget to commit and push your final code to your github.  Both the document and your code have to be submitted on time to get credit.

IMPORTANT: Please keep the github private and add the following users as collaborators: nikunjgoel95, wonsunahn.

Nik, our TA, will record the score for both of you on courseweb, along with feedback on where points have been deducted. You and your partner will get the same score. If you feel otherwise, let me know.

Please post on the discussion board, or email me at wahn@pitt.edu, or come to office hours to discuss any problems you have.
