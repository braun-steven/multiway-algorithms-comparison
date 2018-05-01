% Executes PARAFAC model and component test
%
% Last modified: 20150825
% Last Edited by: Kohji Muraoka

clear all force; close all force;

%% #### END CONFIGURATION #####
fileLoc = 'Fluorescence EEMs'; % should be directly under the current dir
header_size = 3;
RunCompTest = 0; % 1 = run component number test, 0 = do not run
k_max = 10; % max number of components
j_max = 3; % max number of repeats at each components
userComp = 4; % if RunCompTest == 1, the program will ask for the component number
init_method = 2; % Random orthogonalized initialization
scaling = 2; % no output scaling applied
convergence_strategy = 0;
plotting_options = 0;
show_fit = NaN;
%  #### END CONFIGURATION #####

%% file input
addpath(fileLoc);
files = dir(fileLoc);
[m,~] = size(files);

for i=3:m
    filename = files(i,1).name;
    IDo{i-2,1} = filename;

    fid=fopen(filename);
    fseek(fid,0,'bof');

    data_toss = importdata(filename);

    try
        EmAx = data_toss.data(1,:);
        for k = 1:125
            ExAx(k) = str2num(data_toss.textdata{header_size+k,1});
        end
        X(i-2,:,:) = data_toss.data(header_size+1:end,:);
    catch
        EmAx = data_toss(1,2:end);
        ExAx = data_toss(2:end,1);
        X(i-2,:,:) = data_toss(2:end,2:end);
    end

    fclose(fid);
end

%% PARAFAC model and iteration test
addpath('nway');

fprintf('Running parafac...');
for init=1:2
    for i=3:10
        parafac_options = [ convergence_strategy init plotting_options scaling show_fit ];
        [Factors,it,err,corcondia] = parafac(X,i, parafac_options);
        initstr = '';
        if init == 2
            initstr = 'Random Orthogonalized';
        elseif init == 1
            initstr = 'SVD';
        end
        disp(['(', initstr, ')Number of components = ', num2str(i)])
        disp(['(', initstr, ')Loss = ', num2str(err)])
    end
end


%F1 = Factors{1}; % per sample
%F2 = Factors{2}; % per Emmission?
%F3 = Factors{3}; % per Excitation?


%Creates Component Plots
%for i = 1:size(F2,2)
%    figure; contour(EmAx,ExAx,F2(:,i)*F3(:,i)');
%    xlabel('Incident Wavelength (nm)'); ylabel('Fluorescence Wavelength (nm)');
%    figure; surf(EmAx,ExAx,F2(:,i)*F3(:,i)');
%    xlabel('Incident Wavelength (nm)'); ylabel('Fluorescence Wavelength (nm)');
%end
