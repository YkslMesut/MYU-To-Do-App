package com.myu.myuto_do.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myu.myuto_do.data.models.Priority
import com.myu.myuto_do.data.models.ToDoTask
import com.myu.myuto_do.data.repository.DataStoreRepository
import com.myu.myuto_do.data.repository.ToDoRepository
import com.myu.myuto_do.util.Action
import com.myu.myuto_do.util.Constants.MAX_TITLE_LENGTH
import com.myu.myuto_do.util.RequestState
import com.myu.myuto_do.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")


    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask


    private val _allTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _sortState =
        MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )
    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(
                    searchQuery = "%$searchQuery%"
                )
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addTask(todoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    fun readStoreState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect() {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.saveSortState(priority = priority)
        }
    }


    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateTask(toDoTask = todoTask)
        }
    }

    private fun deleteSelectedTask() {
        viewModelScope.launch {
            val todoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.deleteTask(toDoTask = todoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteSelectedTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            Action.NO_ACTION -> {

            }
        }
        this.action.value = Action.NO_ACTION
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    fun updateTaskField(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

}