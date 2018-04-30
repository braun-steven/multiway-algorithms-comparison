## Multiway-Algorithms PARAFAC Comparison Test

To compare the multiway-algorithms PARAFAC implementation with the matlab nway toolbox run the following step:

1. Initialize the mutliway-algorithms and nway-toolbox local installations by running
    ```bash
    $ bash init.sh
    ```
    This should clone the latest multiway-algorithms develop branch and the nway repository
    
2. Run the nway PARAFAC implementation:
    ```bash
    $ bash run-nway-toolbox-parafac.sh
    ```
    Results in:
    ``` 
    Loss =
    
      413.6739
    ```
    
3. Run the multiway-algorithms PARAFAC implementation:
    ```bash
    $ bash run-multiway-alg-parafac.sh
    ```
    Results in:
    ``` 
    [INFO ] 18:14:11.388 [Main.main()] Main - Loss = 413.9942821962213
    ```
    

The data can be found in `matlab/Fluorescence\ EEMs/` as `.dat` files or in `data/` as `.csv` files. The matlab script `matlab/generate_data_dir.m` generates the `.csv` files.

### Algorithm Setup
Both runs are started with the same options:

Option | Value
--- | ---
number of components | 4..10
max. iterations | 2500
improvement tolerance threshold | 10e-6
init method | random orthogonalized matrices
