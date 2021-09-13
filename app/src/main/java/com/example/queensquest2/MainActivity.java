package com.example.queensquest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int queen_count = 0;
    boolean game_won = false;
    final String blank = "blank";
    final String queen = "queen";
    final int num_squares = 64;
    final int num_rows = 8;
    GridLayout chess_board;
    TextView game_status;
//    Toast toast = Toast.makeText( this  , "" , Toast.LENGTH_SHORT );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chess_board = findViewById(R.id.chess_board);
        game_status = findViewById(R.id.status);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int board_width = dm.widthPixels;
        int button_width = (board_width / 8) - 2;

        for(int i = 0; i<num_squares; i++) {
            int rowIdx = i/num_rows;
            int colIdx = i%num_rows;
            Button button = new Button(this);
            button.setId(i);
            button.setWidth(button_width);
            button.setHeight(button_width);
            String tag = blank + rowIdx + colIdx;
            button.setTag(tag);
            button.setOnClickListener(toggle_btn);
            if (rowIdx%2 == 0) {
                if (i%2==0) {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.red));
                } else {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.white));
                }
            } else {
                if (i%2==1) {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.red));
                } else {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.white));
                }
            }
//            chess_board.addView(button);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            GridLayout.Spec row = GridLayout.spec(rowIdx);
            GridLayout.Spec column = GridLayout.spec(colIdx);
            params.columnSpec = column;
            params.rowSpec = row;
            params.width = button_width;
            params.height = button_width;
            chess_board.addView(button, params);

//            Log.v("params", params.toString());
//            params.toString();
        }
    }

    View.OnClickListener toggle_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("button", "button" + v.getId() +"\ntag: " + v.getTag());
            String tag = (String) v.getTag();
            Log.v("button", "button" + v.getId() +"\ntag: " + tag);
            String status = tag.substring(0,5);
            Log.v("button", "button" + v.getId() +"\nstatus: " + status);
            int row = Integer.parseInt(tag.substring(5,6));
            Log.v("button", "button" + v.getId() +"\nrow: " + row);
            int col = Integer.parseInt(tag.substring(6));
            Log.v("button", "button" + v.getId() +"\ncol: " + col);
            int idx = (row*num_rows) + col;
            Log.v("button", "button" + v.getId() +"\nidx: " + idx);

            if (status.equals(queen)) {
                Log.v("button", "button" + v.getId() + "was clicked.\nit was queen.");
                if (row%2 == 0) {
                    if (idx%2==0) {
                        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.red));
                    } else {
                        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.white));
                    }
                } else {
                    if (idx%2==1) {
                        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.red));
                    } else {
                        v.setBackgroundColor(v.getContext().getResources().getColor(R.color.white));
                    }
                }
                String newTag = blank + row + col;
                queen_count--;
                update_status_bar();
                v.setTag(newTag);
            } else if (status.equals(blank)) {
                Log.v("button", "button" + v.getId() + "checking logic");
                if (logic(row, col)) {
                    Log.v("button", "button" + v.getId() + "was clicked.\nit was a blank.");
                    v.setBackgroundColor(v.getContext().getResources().getColor(R.color.purple_500));
                    String newTag = queen + row + col;
                    v.setTag(newTag);
                    check_win_condition();
                    update_status_bar();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text;
                    int duration = Toast.LENGTH_SHORT;
                    text = "Nope!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } else {
                Log.v("button", "button" + v.getId() + " was clicked.\nstatus: " + status);
            }
        }
    };

    public boolean logic(int row, int col) {
        if (diagonal_clear(chess_board, row, col)
                && row_clear(chess_board, row)
                && column_clear(chess_board, col)) {
            return true;
        }
        return false;
    }

    public boolean diagonal_clear(GridLayout chess_board, int row, int col) {

        int checkRow = row;
        int checkCol = col;
        while (checkCol >= 0 && checkRow >= 0) {
            View child = chess_board.getChildAt((checkRow*num_rows) + checkCol); // check first
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
            checkCol--;
            checkRow--;
        }
        checkRow = row;
        checkCol = col;
        while (checkCol < num_rows - 1 && checkRow < num_rows - 1) {
            checkCol++;
            checkRow++;
            View child = chess_board.getChildAt((checkRow*num_rows) + checkCol); // check first
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
        }
        checkRow = row;
        checkCol = col;
        while(checkCol >= 1 && checkRow < num_rows - 1) {
            checkCol--;
            checkRow++;
            View child = chess_board.getChildAt((checkRow*num_rows) + checkCol); // check first
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
        }
        checkRow = row;
        checkCol = col;
        while(checkCol < num_rows - 1 && checkRow >= 1) {
            checkCol++;
            checkRow--;
            View child = chess_board.getChildAt((checkRow*num_rows) + checkCol); // check first
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
        }
        return true;
    }
    public boolean row_clear(GridLayout chess_board, int row) {
        for (int i = 0; i<num_rows; i++) {
            View child = chess_board.getChildAt((row*num_rows) + i);
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
        }
        return true;
    }

    public boolean column_clear(GridLayout chess_board, int col) {
        for (int i = 0; i<num_rows; i++) {
            View child = chess_board.getChildAt((i*num_rows) + col);
            String tag = (String) child.getTag();
            String status = tag.substring(0,5);
            if (status.equals(queen)) {
                return false;
            }
        }
        return true;
    }

    public void check_win_condition() {
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        if (++queen_count == num_rows) {
            game_won = true;
            text = "you won!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            make_toast("good move");
        }
        Log.v("game", "game won");
    }

    public void update_status_bar() {
        int queens_left = num_rows - queen_count;
        game_status.setText(queens_left + " Queens Left!");
    }

    public void reset(View v) {
        game_won = false;
        queen_count = 0;
        update_status_bar();
        for(int i=0; i<num_squares; i++) {
            Button button = (Button) chess_board.getChildAt(i);
            int rowIdx = i/num_rows;
            int colIdx = i%num_rows;
            String tag = blank + rowIdx + colIdx;
            button.setTag(tag);
            make_toast("Reset!");
            if (rowIdx%2 == 0) {
                if (i%2==0) {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.red));
                } else {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.white));
                }
            } else {
                if (i%2==1) {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.red));
                } else {
                    button.setBackgroundColor(button.getContext().getResources().getColor(R.color.white));
                }
            }
        }
    }

    public void make_toast(CharSequence text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}