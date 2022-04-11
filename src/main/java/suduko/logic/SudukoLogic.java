package suduko.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import suduko.models.ValidateCells;

import java.util.*;

public class SudukoLogic {
    private final int boardSize = 9;
    private final int[][] leveldSudukoBoard = new int[boardSize][boardSize];
    private int[][] fullSudokuBoard = new int[boardSize][boardSize];


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
        return leveldSudukoBoard;
    }

    private void createMediumBoard() {
        List<Integer> medium = Arrays.asList(3, 1, 6, 2, 4, 5, 3, 4, 4);
        setLeveldSudukoBoard(medium);
    }

    private void createExpertBoard() {
        List<Integer> expert = Arrays.asList(2, 3, 2, 4, 2, 3, 3, 2, 2);
        setLeveldSudukoBoard(expert);
    }

    private void createEasyBoard() {
        List<Integer> easy = Arrays.asList(2, 4, 3, 6, 5, 5, 4, 4, 5);
        setLeveldSudukoBoard(easy);
    }

    private void setLeveldSudukoBoard(List<Integer> level) {
        Collections.shuffle(level);
        ArrayList<Integer> noneBlankSpaceList;
        for (int i = 0; i < boardSize; i++) {
            noneBlankSpaceList = setNoneBlankSpace(level.get(i));
            for (int j = 0; j < boardSize; j++) {
                leveldSudukoBoard[i][j] = noneBlankSpaceList.contains(j) ? fullSudokuBoard[i][j] : 0;
            }
        }
    }

    private ArrayList<Integer> setNoneBlankSpace(int number) {
        Random rand = new Random();
        ArrayList<Integer> blanks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int nonBlank = rand.nextInt(boardSize);
            if (blanks.contains(nonBlank)) continue;
            blanks.add(nonBlank);
        }
        return blanks;
    }

    private void setNewFullBoard() {
        GameMatrix matrix = Creator.createFull();
        int[][] sudukoBoard = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sudukoBoard[i][j] = matrix.get(i, j);
            }
        }
        fullSudokuBoard = sudukoBoard;
    }

    public ArrayList<ValidateCells> validateInputCells(String body) throws JsonProcessingException {
        ArrayList<ValidateCells> validateCellsArrayList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jNode = mapper.readTree(body);
        for (JsonNode node : jNode) {
            int x = Integer.parseInt(node.get("xPosition").toString());
            int y = Integer.parseInt(node.get("yPosition").toString());
            int numberToValidate = Integer.parseInt(node.get("numberToValidate").toString());
            validateCellsArrayList.add(new ValidateCells(x, y, numberToValidate, validatePosition(x, y, numberToValidate)));
        }
        return validateCellsArrayList;
    }

    private boolean validatePosition(int x, int y, int numberToValidate) {
        return fullSudokuBoard[x][y] == numberToValidate;
    }

    public int[][] getFullSudokuBoard() {
        return fullSudokuBoard;
    }
}
