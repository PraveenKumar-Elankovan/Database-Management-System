This program interfaces with a database using embedded SQL. The program operates on the following database with two relations:
                                             
Course (cid: integer, name:string, capacity: integer)
PrerequisiteCourse (cid: integer, pid:integer)

The key fields are underlined, and the domain of each field is listed after the field name.

The course relation gives information about a course. The prerequisite-Course relation gives prerequisite of a course. That is, if (100,200) is a tuple in prerequisiteCourse then this shows that the course with cid 200 is the prerequisite of the course with cid 100. First, the program creates the tables Course and PrerequisiteCourse. 

After setting up the tables, the program has to read an input file called transfile. Each line in transfile is a transaction. There are six types of transactions. The first character of each line gives the type of the transaction. We call this as transaction code.

•	If the transaction code is 1, then an existing course is deleted. The other field in this line is the id of the course (i.e., the cid) to be deleted. In this case, you need an appropriate tuple to be deleted from the Course table and tuples from the PrerequisiteCourse relation in which this course id appears as pid or cid.

•	If the transaction code is 2, then a new course is inserted. In this case, the other fields, in the line are cid, name, capacity followed by zero or more pids. Each of them is separated by one or more spaces. The first three fields in the line give the course attributes. The subsequent fields give the pids of the prerequisites for the course. Note cid, capacity and pids are integers while name is a string without spaces. For this transaction, a tuple in course relation is inserted and zero or more tuples in prerequisiteCourse relation is inserted. The number of tuples inserted in prerequisiteCourse relation is as many as the number of distinct pids specified in the transaction. Note that the course may not have any prerequisite.

•	If the transaction code is 3, then output of the average capacity of all the courses is displayed.

•	If the transaction code is 4, then output is names of all courses that are prerequisites of a Course directly or indirectly. In this case, the course id is given in the line. For example, if the course id is 100, then you have to output names of courses that are prerequisites of 100, as well as names of courses of their prerequisites and so on.

•	If the transaction code is 5, then output is the average capacity of courses that are direct prerequisites of a Course. If the average capacity is not an integer, round it up to an integer. Here also the course id is given in the input line.

•	If the transaction code is 6, then it is checked if there is any course which has more than one direct prerequisite. In this case, output of the names of all such courses is displayed. If there is no such course then output the string \no courses with more than one prerequisite".


We assume the input file is always valid, and you do not need to check if it is correct. We also assume that each course name is a string of maximum 20 characters without any spaces.

A sample input file is as follows:

***trans file****
2 50 Software 55
2 60 AI 70
2 100 Database 40 50
2 101 Algorithm 53 100 60
4 101
4 50
5 101
6
2

****Your output should be:
done
done
done
done
Software, Database, AI
No prerequisite
55
Algorithm 
