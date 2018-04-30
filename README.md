## Multiway-Algorithms PARAFAC Comparison Test

To compare the multiway-algorithms PARAFAC implementation with the matlab nway toolbox run the following step:

1. Initialize the mutliway-algorithms and nway-toolbox local installations by running
    ```bash
    $ bash init.sh
    ```
    This should clone the latest multiway-algorithms develop branch and the nway repository
2. Run the multiway-algorithms PARAFAC implementation:
    ```bash
    $ bash run-java.sh
    ```
    Results in:
    ``` 
    Loss =
    
      413.6739
    ```
3. Run the nway PARAFAC implementation:
    ```bash
    $ bash run-matlab.sh
    ```
    Results in:
    ``` 
    [INFO ] 18:14:11.388 [Main.main()] Main - Loss = 413.9942821962213
    ```