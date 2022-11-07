package com.ring.project2048

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startGame: TextView
    private lateinit var gameScope: TextView
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGame = findViewById(R.id.startGame)
        gameScope = findViewById(R.id.gameScope)
        gameView = findViewById(R.id.gameView)
        gameView.setGameListener(object : GameView.GameListener {
            override fun refreshScope(scope: Int) {
                gameScope.text = "分数\n$scope"
            }

            override fun gameOver() {
                Toast.makeText(this@MainActivity, "游戏结束啦！", Toast.LENGTH_SHORT).show()
            }

        })
        startGame.setOnClickListener { gameView.startGame() }
    }

    /**
     * 项目功能：2048小游戏
     * 1.背景绘制，需要新游戏开始按钮，2048游戏背景(m*n方格矩阵)，分数按钮
     * 2.数字方块绘制
     * 3.数字随机位置
     * 4.滑动事件监听
     * 5.数字相加、位移逻辑
     * 6.方块滑动动画
     * 7.结束判断
     */

    /**
     * 扩展功能
     * 1.m*n可自由配置
     * 2.根据m、n智能设置首次随机产生方块数量和最大随机产生方块数字
     * 3.添加背景音乐
     */
}