import zipfile
from pathlib import Path
import sys
import shutil

zip_path = Path(sys.argv[1])
outdir = zip_path.parent/"temp"
outdir.mkdir(parents=True, exist_ok=True)
with zipfile.ZipFile(zip_path, "r") as zip_ref:
    zip_ref.extractall(outdir)
    real_out = outdir/zip_path.stem
    shutil.move(str(real_out), str(zip_path.parent/zip_path.stem), copy_function= shutil.copytree)
    outdir.rmdir()