package suduko.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sfuhrm.sudoku.*;
import suduko.models.ValidateCells;

import java.util.*;

public class SudukoLogic {
    private final int boardSize = 9;
    private int[][] validatedCells = new int[boardSize][boardSize];
    private int[][] fullSudokuBoard = new int[boardSize][boardSize];
    //private int[][] validatedCells = new int[boardSize][boardSize];


    public int[][] createNewBoard(String level) {
        setNewFullBoard();
        switch (level) {
            case "expert":
                createExpertBoard();
                break;
            case "medium":
                createMediumBoard();
                break;
            default:
                createEasyBoard();
                break;
        }
        return validatedCells;
    }

    private void createMediumBoard() {
        List<Integer> medium = Arrays.asList(3, 1, 6, 2, 4, 5, 3, 4, 4);
        setValidatedCells(medium);
    }

    private void createExpertBoard() {
        List<Integer> expert = Arrays.asList(2, 3, 2, 4, 2, 3, 3, 2, 2);
        setValidatedCells(expert);
    }

    private void createEasyBoard() {
        List<Integer> easy = Arrays.asList(2, 4, 3, 6, 5, 5, 4, 4, 5);
        setValidatedCells(easy);
    }

    private void setValidatedCells(List<Integer> level) {
        Collections.shuffle(level);
        ArrayList<Integer> noneBlankSpaceList;
        for (int i = 0; i < boardSize; i++) {
            noneBlankSpaceList = setNoneBlankSpace(level.get(i));
            for (int j = 0; j < boardSize; j++) {
                validatedCells[i][j] = noneBlankSpaceList.contains(j) ? fullSudokuBoard[i][j] : 0;
            }
        }
    }

    private ArrayList<Integer> setNoneBlankSpace(int number) {
        Random rand = new Random();
        ArrayList<Integer> blanks = new ArrayList<>();
        while (blanks.size() < number) {
            int nonBlank = rand.nextInt(boardSize);
            if (blanks.contains(nonBlank)) continue;
            blanks.add(nonBlank);
        }
        return blanks;
    }

    private void setNewFullBoard() {
        GameMatrix matrix = Creator.createFull();
        fullSudokuBoard = convertBoard(matrix);
    }

    public ArrayList<ValidateCells> validateInputCells(String body) throws JsonProcessingException {
        ArrayList<ValidateCells> validateCellsArrayList = new ArrayList<>();
        int[][] templist = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                templist[i][j] = validatedCells[i][j];
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jNode = mapper.readTree(body);
        for (JsonNode node : jNode) {
            int x = Integer.parseInt(node.get("xPosition").toString());
            int y = Integer.parseInt(node.get("yPosition").toString());
            int numberToValidate = Integer.parseInt(node.get("numberToValidate").toString());
            //Lista som kollar true or false

            validateCellsArrayList.add(new ValidateCells(x, y, numberToValidate));
            templist[x][y] = numberToValidate;

        }

        //köra tempolist emottemplist = {int[9][]@3237}


        return validatePosition(templist, validateCellsArrayList);
    }

    private ArrayList<ValidateCells> validatePosition(int[][] templist, ArrayList<ValidateCells> validateCellsArrayList) throws JsonProcessingException {
        System.out.println(validatedCells.length);
        List<GameMatrix> solutions = checkSudukoBoard(convertIntToByte(validatedCells));
        int highestIndex = 0;
        int highestCorrectNr = 0;

        for (int i = 0; i < solutions.size(); i++) {
            int correct = validateSolution(solutions.get(i), templist);
            if (correct > highestCorrectNr) {
                highestIndex = i;
                highestCorrectNr = correct;
            }
        }
        return validateNumber(solutions.get(highestIndex), validateCellsArrayList);
    }

    private ArrayList<ValidateCells> validateNumber(GameMatrix matrix, ArrayList<ValidateCells> validateCellsArrayList) {
        for (ValidateCells cell : validateCellsArrayList
        ) {
            if (convertBoard(matrix)[cell.getxPosition()][cell.getyPosition()] == cell.getNumberToValidate()) {
                cell.setCorrectNumber(true);
                validatedCells[cell.getxPosition()][cell.getyPosition()] = cell.getNumberToValidate();
            } else {
                cell.setCorrectNumber(false);
            }
        }
        return validateCellsArrayList;
    }

    private int validateSolution(GameMatrix solution, int[][] templist) {
        int correctNumbers = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (convertBoard(solution)[i][j] == templist[i][j]) correctNumbers++;
            }
        }

        return correctNumbers;
    }

    public List<GameMatrix> checkSudukoBoard(byte[][] board) throws JsonProcessingException {
        Riddle riddle = new GameMatrixFactory().newRiddle();
        riddle.setAll(board);
        Solver solver = new Solver(riddle);
        return solver.solve(); // Skickar tillbaka mina solutions
    }

    public int[][] solveCurrentBoard(String ctxBody) throws JsonProcessingException {
        List<GameMatrix> solutions = checkSudukoBoard(convertIncomingBoard(ctxBody));
        return convertBoard(solutions.get(0));
    }

    private byte[][] convertIncomingBoard(String ctxBody) throws JsonProcessingException {
        byte[][] tempList = new byte[boardSize][boardSize];
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jNode = mapper.readTree(ctxBody);

        int i = 0;
        for (JsonNode node : jNode) {
            for (int j = 0; j < boardSize; j++) {
                tempList[i][j] = Byte.parseByte(node.get(j).toString());
            }
            i++;
        }
        return tempList;
    }


    private int[][] convertBoard(GameMatrix matrix) {
        int[][] sudukoBoard = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sudukoBoard[i][j] = matrix.get(i, j);
            }
        }
        return sudukoBoard;
    }

    private byte[][] convertIntToByte(int[][] temp) {
        byte[][] sudukoBoard = new byte[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sudukoBoard[i][j] = (byte) temp[i][j];
            }
        }
        return sudukoBoard;
    }


}
