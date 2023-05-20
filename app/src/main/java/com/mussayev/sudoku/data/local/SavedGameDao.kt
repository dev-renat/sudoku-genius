package com.mussayev.sudoku.data.local

//@Dao
//interface SavedGameDao {
//    @Query("SELECT * FROM saved_games WHERE id = :gameId")
//    suspend fun getSavedGameById(gameId: Int): SavedGameEntity?
//
//    @Query("SELECT * FROM saved_games ORDER BY id DESC LIMIT 1")
//    suspend fun getLastSavedGame(): SavedGameEntity?
//
//    @Query("SELECT * FROM saved_games WHERE difficulty == :difficulty")
//    suspend fun getSavedGameByDifficulty(difficulty: String): SavedGameEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveGame(game: SavedGameEntity): Long
//
//    @Update
//    suspend fun update(gameEntity: SavedGameEntity)
//
//    @Delete
//    suspend fun deleteGame(game: SavedGameEntity)
//
//    @Query("DELETE FROM saved_games WHERE difficulty = :difficulty")
//    suspend fun deleteGamesByDifficulty(difficulty: String)
//
//    @Transaction
//    suspend fun updateGameInTransaction(game: SavedGameEntity) {
//        update(game)
//    }
//}


//@Dao
//interface SavedGameDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(noteModel: SavedGameEntity)
//
//    @Insert
//    fun insertAll(users: List<SavedGameEntity>)
//
//    @Query("SELECT * FROM sudoku_table")
//    fun getAllSudoku(): List<SavedGameEntity>
//
//    @Query("SELECT * FROM sudoku_table WHERE level == :level")
//    fun getByLevel(level: Int): LiveData<SavedGameEntity>
//
//    @Delete
//    suspend fun delete(noteModel: SavedGameEntity)
//
//    @Query("DELETE FROM sudoku_table WHERE level == :level")
//    suspend fun deleteByLevel(level: Int)
//}

