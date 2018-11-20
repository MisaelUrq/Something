import os
import sys

path_of_src="\\src\\com\\urquieta\\something\\platform"

def get_files_to_modify(directory):
    result = []
    complete_path = os.getcwd() + directory
    for element in os.listdir(complete_path):
        complete_file_path = os.path.join(complete_path, element)
        if os.path.isfile(complete_file_path):
            result.append(complete_file_path)

    return result


# NOTE(Misael): The code assumes that you are already in a separate
# build, if it's in the same one, it will output the wrong thing
def change_file_to_platform(dest_platform, original_platform, file_path):
    lines = []
    f = open(file_path, 'r')
    save_line = ""
    for line in f:
        lookup_symbol = '// @'+dest_platform
        if line.startswith(lookup_symbol, 0, len(line)):
            line_split = line.split('<>')
            save_line = line_split[1].rstrip() + line_split[0] + '\n'
            lines.append(save_line)
        elif ('// @'+original_platform) in line:
            line_split = line.split(';')
            save_line = line_split[1].rstrip() + '<>' + line_split[0] + ';\n'
            lines.append(save_line)
        elif '// @Class' in line:
            lines.append(line.replace(original_platform, dest_platform))
        else:
            lines.append(line)

    tmp = open(file_path, 'w')
    for line in lines:
        tmp.write(line)


def modify_files_to_platform(platform, files):
    if platform == 'Android':
        for file_path in files:
            change_file_to_platform(platform, 'PC', file_path)
    elif platform == 'PC':
        for file_path in files:
            change_file_to_platform(platform, 'Android', file_path)
    else:
        print("ERROR: Platform not supported")

if __name__=='__main__':
    if len(sys.argv) > 1:
        modify_files_to_platform(sys.argv[1], get_files_to_modify(path_of_src))
